package dev.mbo.t60f.domain.request

import dev.mbo.logging.logger
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@Component
class FeedbackRequestTask(
    private val repository: FeedbackRequestRepository,
) {
    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.MINUTES)
    fun deletePendingRequests() {
        val ts = Instant.now().minus(60, ChronoUnit.MINUTES)
        log.info("cleanup requests before {}", ts)
        repository.findByCreatedAtIsBefore(ts = ts).forEach {
            repository.delete(it)
        }
    }

}