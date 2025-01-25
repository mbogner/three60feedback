package dev.mbo.t60f.domain.round

import dev.mbo.t60f.domain.giver.FeedbackGiver
import dev.mbo.t60f.domain.giver.FeedbackGiverRepository
import dev.mbo.t60f.domain.request.FeedbackRequest
import dev.mbo.t60f.domain.request.FeedbackRequestRepository
import dev.mbo.t60f.domain.user.UserRepository
import dev.mbo.t60f.logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@Transactional(readOnly = true)
@Service
class FeedbackRoundService(
    private val roundRepository: FeedbackRoundRepository,
    private val giverRepository: FeedbackGiverRepository,
    private val userRepository: UserRepository,
    private val feedbackRequestRepository: FeedbackRequestRepository,
) {

    private val log = logger()

    @Transactional
    fun create(feedbackRequest: FeedbackRequest, receiver: String, invites: List<String>) {
        val receivingUser = userRepository.findByEmail(receiver)!!

        val round = roundRepository.save(
            FeedbackRound(
                receiver = receivingUser,
            )
        )
        invites.forEach { email ->
            round.givers.plus(giverRepository.save(FeedbackGiver(feedbackRound = round, email = email)))
        }

        feedbackRequestRepository.delete(feedbackRequest)
    }

    @Transactional
    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    fun cleanup() {
        val ts = Instant.now().minus(30, ChronoUnit.DAYS)
        log.debug("cleanup before {}", ts)
        roundRepository.findByCreatedAtIsBefore(ts).forEach { round ->
            log.info("cleanup delete round {}", round)
            roundRepository.delete(round)
        }
    }

}