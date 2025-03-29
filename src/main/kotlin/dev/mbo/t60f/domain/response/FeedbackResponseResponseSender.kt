package dev.mbo.t60f.domain.response

import dev.mbo.logging.logger
import dev.mbo.t60f.global.AsyncMailSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class FeedbackResponseResponseSender(
    private val feedbackResponseRepository: FeedbackResponseRepository,
    private val mailer: AsyncMailSender,
    @Value("\${app.base-url}")
    private val baseUrl: String,
) {

    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun sendRequests() {
        feedbackResponseRepository.findOpenResponses().forEach {
            try {
                it.notifiedAt = Instant.now()
                sendResponse(it)
            } catch (e: Exception) {
                it.notifyFailed = true
                log.error("Failed to send feedback response ${it.id}", e)
            } finally {
                feedbackResponseRepository.save(it)
            }
        }
    }

    fun sendResponse(giver: FeedbackResponse) {
        log.info("sending feedback response {}", giver)
        mailer.send(
            to = giver.feedbackRound.receiver.email,
            subject = "You Received Feedback",
            content = """
            Hi ${giver.feedbackRound.receiver.email}!
            
            somebody handed in a feedback form for you. Here is the content:
            
            --------------
            Positive Feedback:
            ${giver.positiveFeedback}
            --------------
            Improvement Suggestions:
            ${giver.negativeFeedback}
            --------------
            
            Click this link to report the feedback: $baseUrl/response/${giver.id}/report
            
            Yours,
            t60f
            """.trimIndent()
        )
    }

}