package dev.mbo.t60f.domain.round

import dev.mbo.t60f.domain.request.FeedbackRequest
import dev.mbo.t60f.domain.request.FeedbackRequestRepository
import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Transactional(readOnly = true)
@Service
class FeedbackRoundService(
    private val roundRepository: FeedbackRoundRepository,
    private val giverRepository: FeedbackResponseRepository,
    private val userRepository: UserRepository,
    private val feedbackRequestRepository: FeedbackRequestRepository,
) {

    @Transactional
    fun create(
        feedbackRequest: FeedbackRequest,
        proxy: String?,
        receiver: String,
        invites: List<String>,
        days: Long,
        focus: String?,
        companyId: UUID,
        loggedInUsername: String?
    ) {
        val receivingUser = userRepository.findByEmailAndCompanyId(receiver, companyId)
            ?: throw IllegalStateException("receiver not found within company")
        val receivingProxy = if (proxy.isNullOrBlank()) null else {
            userRepository.findByEmailAndCompanyId(proxy, companyId)
                ?: throw IllegalStateException("proxy receiver not found within company")
        }
        if (receivingProxy != null && loggedInUsername != receivingProxy.email) {
            throw IllegalStateException("receiving proxy has to be the logged in user")
        }

        val round = roundRepository.save(
            FeedbackRound(
                receiver = receivingUser,
                proxyReceiver = receivingProxy,
                validity = Instant.now().plus(days, ChronoUnit.DAYS),
                focus = focus,
            ),
        )
        invites.forEach { email ->
            round.givers.plus(giverRepository.save(FeedbackResponse(feedbackRound = round, email = email)))
        }

        feedbackRequestRepository.delete(feedbackRequest)
    }

}