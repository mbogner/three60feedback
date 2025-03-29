package dev.mbo.t60f.domain.round

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.request.FeedbackRequest
import dev.mbo.t60f.domain.request.FeedbackRequestRepository
import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Transactional(readOnly = true)
@Service
class FeedbackRoundService(
    private val roundRepository: FeedbackRoundRepository,
    private val giverRepository: FeedbackResponseRepository,
    private val userRepository: UserRepository,
    private val feedbackRequestRepository: FeedbackRequestRepository,
) {

    private val log = logger()

    @Transactional
    fun create(feedbackRequest: FeedbackRequest, receiver: String, invites: List<String>, days: Int) {
        val receivingUser = userRepository.findByEmail(receiver)!!

        val round = roundRepository.save(
            FeedbackRound(
                receiver = receivingUser,
                validity = Instant.now().plus(8, ChronoUnit.DAYS)
            ),
        )
        invites.forEach { email ->
            round.givers.plus(giverRepository.save(FeedbackResponse(feedbackRound = round, email = email)))
        }

        feedbackRequestRepository.delete(feedbackRequest)
    }

}