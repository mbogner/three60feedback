package dev.mbo.t60f.domain.response.message

import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.global.AsyncMailSender
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class FeedbackResponseMessageService(
    private val responseRepository: FeedbackResponseRepository,
    private val messageRepository: FeedbackResponseMessageRepository,
    private val mailer: AsyncMailSender,
    @Value("\${app.base-url}") private val baseUrl: String,
) {

    fun findByResponseId(loginMail: String, responseId: UUID): FeedbackResponse {
        val response = responseRepository.findByIdWithMessages(responseId)
            ?: throw EntityNotFoundException("no round found with id $responseId")
        require(response.positiveFeedback != null && response.negativeFeedback != null) {
            "not available if feedback hasn't been entered yet"
        }
        require(
            response.feedbackRound.receiver.email == loginMail
                    || response.feedbackRound.proxyReceiver?.email == loginMail
                    || response.email == loginMail
        ) {
            "you have to be the receiver, proxy or the person who wrote the feedback"
        }
        response.messages = response.messages.sortedByDescending { it.createdAt }
        return response
    }

    @Transactional
    fun addMessage(loginMail: String, responseId: UUID, message: String) {
        val response = findByResponseId(loginMail, responseId)
        messageRepository.save(
            FeedbackResponseMessage(
                feedbackResponse = response,
                senderMail = loginMail,
                message = message,
            )
        )
    }

    @Transactional
    fun sendMail(responseMessage: FeedbackResponseMessage) {
        val to = calculateTo(responseMessage.senderMail, responseMessage)
        val content = """
Hi $to,

${responseMessage.senderMail} has added a message to the feedback for ${responseMessage.feedbackResponse.feedbackRound.receiver.email}:

${responseMessage.message}

You can click the link 
$baseUrl/my/response/${responseMessage.feedbackResponse.id}/questions?source=mail
to answer.

Yours,
t60f
""".trimIndent()

        mailer.send(to = to, subject = "Feedback Message", content = content)
        responseMessage.messageSentAt = Instant.now()
        responseMessage.messageSendFails = 0
        messageRepository.save(responseMessage)
    }

    companion object {
        fun calculateTo(loginMail: String, message: FeedbackResponseMessage): String {
            val response = message.feedbackResponse
            val round = response.feedbackRound

            // no proxy case
            return if (round.proxyReceiver == null) {
                if (loginMail == round.receiver.email) {
                    response.email
                } else {
                    round.receiver.email
                }
            } else {
                // proxy case
                if (loginMail == round.proxyReceiver!!.email) {
                    response.email
                } else {
                    round.proxyReceiver!!.email
                }
            }
        }
    }

}