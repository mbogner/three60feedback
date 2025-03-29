package dev.mbo.t60f.domain.round

import dev.mbo.logging.logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Component
class FeedbackRoundTask(
    private val roundRepository: FeedbackRoundRepository,
) {

    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    fun cleanup() {
        log.info("cleanup rounds")
        roundRepository.findByValidityIsBefore().forEach { round ->
            log.info("cleanup: delete round {}", round)
            roundRepository.delete(round)
        }
    }

}