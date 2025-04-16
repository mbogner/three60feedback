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

    fun sendResponse(response: FeedbackResponse) {
        log.info("sending feedback response {}", response)

        val proxy = response.feedbackRound.proxyReceiver
        val feedbackReceiver = response.feedbackRound.receiver.email
        val mailReceiver = proxy?.email ?: feedbackReceiver

        mailer.send(
            to = mailReceiver,
            subject = "Feedback for $feedbackReceiver",
            content = """
Hi $mailReceiver!

somebody handed in a feedback form for $feedbackReceiver. Here is the content:

--------------
Positive Feedback:
${response.positiveFeedback}
--------------
Improvement Suggestions:
${response.negativeFeedback}
--------------

Click this link to report the feedback: $baseUrl/response/${response.id}/report
You can also undo the report by clicking: $baseUrl/response/${response.id}/unreport

Yours,
t60f""".trimIndent()
        )
    }

}