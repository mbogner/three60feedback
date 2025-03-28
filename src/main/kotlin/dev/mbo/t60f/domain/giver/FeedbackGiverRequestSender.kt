package dev.mbo.t60f.domain.giver

import dev.mbo.logging.logger
import dev.mbo.t60f.global.AsyncMailSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class FeedbackGiverRequestSender(
    private val feedbackGiverRepository: FeedbackGiverRepository,
    private val mailer: AsyncMailSender,
    @Value("\${app.base-url}")
    private val baseUrl: String,
) {

    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun sendRequests() {
        feedbackGiverRepository.findAllBySentAtIsNull().forEach {
            try {
                it.sentAt = Instant.now()
                sendRequest(it)
            } catch (e: Exception) {
                it.sendFailed = true
                log.error("Failed to send feedback giver ${it.id}", e)
            } finally {
                feedbackGiverRepository.save(it)
            }
        }
    }

    fun sendRequest(giver: FeedbackGiver) {
        log.info("sending feedback request {}", giver)
        mailer.send(
            to = giver.email,
            subject = "Feedback Request from ${giver.feedbackRound.receiver.email}",
            content = """
            Hi ${giver.email}!
            
            ${giver.feedbackRound.receiver.email} is requesting feedback by you. The system will store who handed in 
            what to avoid abuse. The receiver won't see who sent which feedback.
            
            Please follow this link to give feedback: ${baseUrl}/response/${giver.id}
            
            Be advised that the page behind the link won't display the name of the requestor anymore for privacy reason.
            You can can only hand in one feedback per request.
            
            Thanks in advance!
            
            Yours,
            t60f
            """.trimIndent()
        )
    }

}