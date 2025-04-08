package dev.mbo.t60f.domain.round

import com.fasterxml.jackson.databind.ObjectMapper
import dev.mbo.logging.logger
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service
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
The senders are also next to their response.
Please provide an English summary of positive and constructive feedback that we can show to the receiver.
Do not mention the names of the persons giving feedback.
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
    )

    data class Summary(
        val text: String? = "no feedback to summarise",
        val requested: Int,
        val responded: Int = 0,
    )

    private val log = logger()

    fun createSummary(round: FeedbackRound): Summary {
        val responses = round.givers
        val received = responses.filter { it.positiveFeedback != null && it.negativeFeedback != null }
            .map { Response(from = it.email, positive = it.positiveFeedback!!, constructive = it.negativeFeedback!!) }
        if (received.isEmpty()) {
            return Summary(requested = responses.size)
        }
        val tmpRound = Round(
            receiver = round.receiver.email,
            responses = received,
        )
        val json = mapper.writeValueAsString(tmpRound)
        val enrichedPrompt = PROMPT + json
        return Summary(
            text = chatClient.prompt(enrichedPrompt).call().content(),
            requested = responses.size,
            responded = received.size
        )
    }

    fun createSummary(roundId: UUID): Summary {
        val round = feedbackRoundRepository.findByIdWithResponses(roundId)
            ?: throw IllegalArgumentException("No round with id $roundId")
        return createSummary(round)
    }
}
