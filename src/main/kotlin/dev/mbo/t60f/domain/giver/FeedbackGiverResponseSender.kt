package dev.mbo.t60f.domain.giver

import dev.mbo.t60f.global.AsyncMailSender
import dev.mbo.t60f.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class FeedbackGiverResponseSender(
    private val feedbackGiverRepository: FeedbackGiverRepository,
    private val mailer: AsyncMailSender,
    @Value("\${app.base-url}")
    private val baseUrl: String,
) {

    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun sendRequests() {
        feedbackGiverRepository.findOpenResponses().forEach {
            try {
                it.notifiedAt = Instant.now()
                sendResponse(it)
            } catch (e: Exception) {
                it.notifyFailed = true
                log.error("Failed to send feedback response ${it.id}", e)
            } finally {
                feedbackGiverRepository.save(it)
            }
        }
    }

    fun sendResponse(giver: FeedbackGiver) {
        log.info("sending feedback response {}", giver)
        mailer.send(
            to = giver.feedbackRound.receiver.email,
            subject = "You Received Feedback",
            content = """
            Hi ${giver.feedbackRound.receiver.email}!
            
            somebody handed in a feedback form for you. Here is the content:
            
            ${giver.positiveFeedback}
            
            ${giver.negativeFeedback}
            
            Yours,
            t60f
            """.trimIndent()
        )
    }

}