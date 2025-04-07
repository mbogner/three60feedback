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
class FeedbackResponseRequestSender(
    private val feedbackResponseRepository: FeedbackResponseRepository,
    private val mailer: AsyncMailSender,
    @Value("\${app.base-url}")
    private val baseUrl: String,
) {

    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun sendRequests() {
        feedbackResponseRepository.findAllBySentAtIsNull().forEach {
            try {
                it.sentAt = Instant.now()
                sendRequest(it)
            } catch (e: Exception) {
                it.sendFailed = true
                log.error("Failed to send feedback giver ${it.id}", e)
            } finally {
                feedbackResponseRepository.save(it)
            }
        }
    }

    fun sendRequest(response: FeedbackResponse) {
        log.info("sending feedback request {}", response)

        val proxyMail = response.feedbackRound.proxyReceiver?.email
        val byStr = if(null == proxyMail) "" else " by $proxyMail"
        val optionalFocus: String = if (!response.feedbackRound.focus.isNullOrBlank()) {
            """

The following focus was added to the request:
--------------
${response.feedbackRound.focus}
--------------

""".trimIndent()
        } else {
            ""
        }

        mailer.send(
            to = response.email,
            subject = "Feedback Request for ${response.feedbackRound.receiver.email}",
            content = """
Hi ${response.email}!

feedback for ${response.feedbackRound.receiver.email} has been requested from you$byStr.
The system will store your input to avoid abuse. No receiver will see the sender.

Please follow this link to give feedback: ${baseUrl}/response/${response.id}
$optionalFocus
Be advised that the page behind the link won't display any more details for privacy reason.
You can can only hand in one feedback per request.

Thanks in advance!

Yours,
t60f
""".trimIndent()
        )
    }

}