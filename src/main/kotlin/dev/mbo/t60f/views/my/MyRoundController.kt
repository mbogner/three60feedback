package dev.mbo.t60f.views.my

import dev.mbo.t60f.domain.round.FeedbackRound
import dev.mbo.t60f.domain.round.FeedbackRoundRepository
import dev.mbo.t60f.domain.round.FeedbackRoundSummaryService
import jakarta.transaction.Transactional
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Controller
@RequestMapping("/my/rounds")
class MyRoundController(
    private val summaryService: FeedbackRoundSummaryService,
    private val feedbackRoundRepository: FeedbackRoundRepository,
) {

    @GetMapping
    fun rounds(
        model: ModelMap,
        authentication: Authentication?
    ): String {
        if (authentication == null) throw IllegalArgumentException("authentication must not be null")
        val rounds: List<FeedbackRound> = feedbackRoundRepository.findAllByReceiverEmail(authentication.name)
        model.addAttribute("rounds", rounds)
        return "my/rounds"
    }

    @GetMapping("/proxy")
    fun proxyRounds(
        model: ModelMap,
        authentication: Authentication?
    ): String {
        if (authentication == null) throw IllegalArgumentException("authentication must not be null")
        val rounds: List<FeedbackRound> = feedbackRoundRepository.findAllByProxyEmail(authentication.name)
        model.addAttribute("rounds", rounds)
        return "my/proxy_rounds"
    }

    @GetMapping("/{roundId}/overview")
    fun overview(
        @PathVariable("roundId") roundId: UUID,
        @RequestParam(value = "source", required = false) source: String? = null,
        model: ModelMap,
        authentication: Authentication?
    ): String {
        if (authentication == null) throw IllegalArgumentException("authentication must not be null")
        val round = feedbackRoundRepository.findByIdWithResponses(roundId)
        require(
            round?.receiver?.email == authentication.name
                    || round?.proxyReceiver?.email == authentication.name
        ) { "not allowed to see this round" }

        val responses = round.givers
            .filter { it.positiveFeedback != null && it.negativeFeedback != null } // sorted by createdAt desc
            .reversed()
        model.addAttribute("responses", responses)
        model.addAttribute("source", source)
        return "my/round_overview"
    }

    @Transactional
    @GetMapping("/{roundId}/summary")
    fun summary(
        @PathVariable("roundId") roundId: UUID,
        model: ModelMap,
        authentication: Authentication?,
        @RequestParam(value = "source", required = false) source: String? = null,
    ): String {
        if (authentication == null) throw IllegalArgumentException("authentication must not be null")
        val roles = authentication.authorities.map { it.authority }
        if (roles.isEmpty()) throw IllegalArgumentException("roles must not be null")

        val round: FeedbackRound = summaryService.loadRound(roundId)
        if (
            round.receiver.email != authentication.name
            && !roles.contains("ROLE_ADMIN")
            && !roles.contains("ROLE_COACH")
        )
            throw IllegalArgumentException("can't open other's summary without proper role")

        val summary = summaryService.createSummary(round)
        model.addAttribute("summary", summary)
        model.addAttribute("round", round)
        model.addAttribute("source", source)
        return "my/round_feedback_summary"
    }

}