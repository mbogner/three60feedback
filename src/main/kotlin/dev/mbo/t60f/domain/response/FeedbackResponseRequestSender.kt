package dev.mbo.t60f.domain.response

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.round.FeedbackRoundService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class FeedbackResponseRequestSender(
    private val feedbackResponseRepository: FeedbackResponseRepository,
    private val feedbackRoundService: FeedbackRoundService,
) {

    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun sendRequests() {
        feedbackResponseRepository.findAllBySentAtIsNull().forEach {
            try {
                it.sentAt = Instant.now()
                feedbackRoundService.sendRequest(it)
            } catch (e: Exception) {
                it.sendFailed = true
                log.error("Failed to send feedback giver ${it.id}", e)
            } finally {
                feedbackResponseRepository.save(it)
            }
        }
    }


}