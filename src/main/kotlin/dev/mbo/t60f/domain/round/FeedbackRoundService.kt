package dev.mbo.t60f.domain.round

import dev.mbo.t60f.domain.request.FeedbackRequest
import dev.mbo.t60f.domain.request.FeedbackRequestRepository
import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
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
                validity = Instant.now()
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate()
                    .plusDays(days + 1)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant(),
                focus = focus,
            ),
        )
        invites.forEach { email ->
            round.givers.plus(giverRepository.save(FeedbackResponse(feedbackRound = round, email = email)))
        }

        feedbackRequestRepository.delete(feedbackRequest)
    }

    @Transactional
    fun extendValidity(roundId: UUID, amount: Long = 1L, timeUnit: TemporalUnit = ChronoUnit.DAYS) {
        val round = loadRound(roundId)
        onlyAllowForNotEndedRound(round)
        round.validity = round.validity.plus(amount, timeUnit)
        roundRepository.save(round)
    }

    @Transactional
    fun shortenValidity(roundId: UUID, amount: Long = 1L, timeUnit: TemporalUnit = ChronoUnit.DAYS) {
        val round = loadRound(roundId)
        onlyAllowForNotEndedRound(round)
        val newValidity = round.validity.minus(amount, timeUnit)
        if (newValidity.isAfter(Instant.now())) {
            round.validity = newValidity
            roundRepository.save(round)
        } else {
            throw IllegalStateException("validity can't be in the past")
        }
    }

    private fun loadRound(roundId: UUID): FeedbackRound {
        return roundRepository.findByIdOrNull(roundId) ?: throw IllegalStateException("round $roundId not found")
    }

    private fun onlyAllowForNotEndedRound(round: FeedbackRound) {
        if (round.summaryMailed) {
            throw IllegalStateException("not allowed to shorten or extend a finished round")
        }
    }

}
