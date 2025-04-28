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
                validity = tsPlusDaysStartOfDay(days + 1),
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
        round.validity = tsPlusDaysStartOfDay(days = 1, ts = round.validity)
        roundRepository.save(round)
    }

    @Transactional
    fun shortenValidity(roundId: UUID) {
        val round = loadRound(roundId)
        onlyAllowForNotEndedRound(round)
        val newValidity = tsPlusDaysStartOfDay(days = -1, ts = round.validity)
        if (newValidity.isAfter(Instant.now())) {
            round.validity = newValidity
            roundRepository.save(round)
        } else {
            throw IllegalStateException("validity can't be in the past")
        }
    }

    @Transactional
    fun reopen(roundId: UUID) {
        val round = loadRound(roundId)
        if (!round.summaryMailed) {
            throw IllegalStateException("Can not reopen open round")
        }
        round.summaryMailed = false
        round.validity = tsPlusDaysStartOfDay(8)
        roundRepository.save(round)
    }

    @Transactional
    fun end(roundId: UUID) {
        val round = loadRound(roundId)
        onlyAllowForNotEndedRound(round)
        round.validity = Instant.now()
        roundRepository.save(round)
    }

    private fun loadRound(roundId: UUID): FeedbackRound {
        return roundRepository.findByIdOrNull(roundId) ?: throw IllegalStateException("round $roundId not found")
    }

    private fun onlyAllowForNotEndedRound(round: FeedbackRound) {
        if (round.summaryMailed) {
            throw IllegalStateException("not allowed for a finished round")
        }
    }

    private fun tsPlusDaysStartOfDay(
        days: Long,
        ts: Instant = Instant.now(),
    ): Instant {
        return ts
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plusDays(days)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
    }

}
