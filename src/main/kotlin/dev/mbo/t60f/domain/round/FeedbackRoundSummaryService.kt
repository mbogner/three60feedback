package dev.mbo.t60f.domain.round

import com.fasterxml.jackson.databind.ObjectMapper
import dev.mbo.logging.logger
import dev.mbo.t60f.domain.response.message.FeedbackResponseMessage
import jakarta.transaction.Transactional
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Suppress("unused")
@Service
class FeedbackRoundSummaryService(
    private val feedbackRoundRepository: FeedbackRoundRepository,
    private val chatClient: ChatClient,
    private val mapper: ObjectMapper,
) {

    companion object {
        private val PROMPT = """
We gathered positive and constructive feedback from different persons for the receiver mentioned in the json.
The senders are also next to their response. Further there is the option to discuss every feedback between the receiver
of the feedback and the sender of the feedback.
Please provide an English summary of positive and constructive feedback that we can show to the receiver. Also take in
account the messages the persons exchanged for a given feedback response.
Do not mention any names in your summary.
You also don't have to include a the receiver's name in a separate field because that is already displayed next 
to your answer.
Stay polite but don't hold back negative points.
Please answer in proper html that i can then integrate into a bootstrap html file within a paragraph.
Do not start with ```html or end with ```. We are not talking markdown.
If there hasn't been any feedback in responses or you have too little information to create a summary from just write
"no feedback to summarise".


        """.trimIndent()
    }

    data class Round(
        val receiver: String,
        val responses: List<Response>,
    )

    data class Response(
        val from: String,
        val positive: String,
        val constructive: String,
        val messages: List<Message> = emptyList(),
    )

    data class Message(
        val sender: String,
        val content: String,
        val createdAt: Instant,
    )

    data class Summary(
        val receiver: String,
        val requested: Int,
        val responded: Int = 0,
        val roundCreatedAt: Instant,
    )

    private val log = logger()

    @Transactional(Transactional.TxType.MANDATORY)
    fun createSummary(round: FeedbackRound): Summary {
        val responses = round.givers
        val received = responses.filter { it.positiveFeedback != null && it.negativeFeedback != null }
            .map {
                Response(
                    from = it.email,
                    positive = it.positiveFeedback!!,
                    constructive = it.negativeFeedback!!,
                    messages = it.messages.map { message: FeedbackResponseMessage ->
                        Message(
                            sender = message.senderMail,
                            content = message.message,
                            createdAt = message.createdAt!!
                        )
                    }
                )
            }
        if (received.isEmpty()) {
            return Summary(
                receiver = round.receiver.email,
                requested = responses.size,
                roundCreatedAt = round.createdAt!!
            )
        }
        // use cached value for 5 minutes
        if (null != round.summary
            && null != round.summaryTs
            && round.summaryTs!!.isAfter(Instant.now().minusSeconds(5 * 60))
        ) {
            return Summary(
                receiver = round.receiver.email,
                requested = responses.size,
                responded = received.size,
                roundCreatedAt = round.createdAt!!
            )
        }

        // update summary
        val tmpRound = Round(
            receiver = round.receiver.email,
            responses = received,
        )
        val json = mapper.writeValueAsString(tmpRound)
        val enrichedPrompt = PROMPT + json
        log.debug("summary prompt:\n{}", enrichedPrompt)

        round.summary = chatClient.prompt(enrichedPrompt).call().content()
        round.summaryTs = Instant.now()
        log.debug("response: {}", round.summary)
        return Summary(
            receiver = round.receiver.email,
            requested = responses.size,
            responded = received.size,
            roundCreatedAt = round.createdAt!!
        )
    }

    fun loadRound(roundId: UUID): FeedbackRound {
        return feedbackRoundRepository.findByIdWithResponsesAndMessages(roundId)
            ?: throw IllegalArgumentException("No round with id $roundId")
    }

    @Transactional(Transactional.TxType.MANDATORY)
    fun createSummary(roundId: UUID): Summary {
        return createSummary(loadRound(roundId))
    }
}
