package dev.mbo.t60f.views.my

import dev.mbo.t60f.domain.response.message.FeedbackResponseMessageService
import dev.mbo.t60f.domain.round.FeedbackRound
import dev.mbo.t60f.domain.round.FeedbackRoundRepository
import dev.mbo.t60f.domain.round.FeedbackRoundService
import dev.mbo.t60f.domain.round.FeedbackRoundSummaryService
import jakarta.transaction.Transactional
import jakarta.validation.constraints.Size
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID
import javax.validation.Valid

@Controller
@RequestMapping("/my")
class MyRoundController(
    private val summaryService: FeedbackRoundSummaryService,
    private val feedbackRoundRepository: FeedbackRoundRepository,
    private val feedbackRoundService: FeedbackRoundService,
    private val service: FeedbackResponseMessageService,
) {

    @GetMapping("/rounds")
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

    @GetMapping("/{source}/{roundId}/overview")
    fun overview(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("source") source: String,
        model: ModelMap,
        authentication: Authentication?
    ): String {
        if (authentication == null) throw IllegalArgumentException("authentication must not be null")
        val round = feedbackRoundRepository.findByIdWithResponsesAndMessages(roundId)
        if (null == round) {
            throw IllegalArgumentException("round with id $roundId not found in database")
        }
        require(
            round.receiver.email == authentication.name
                    || round.proxyReceiver?.email == authentication.name
        ) { "not allowed to see this round" }

        val responses = round.givers
            .filter { it.positiveFeedback != null && it.negativeFeedback != null }
            .sortedBy { it.notifiedAt }
        model.addAttribute("responses", responses)
        model.addAttribute("source", source)
        return "my/round_overview"
    }

    @Transactional
    @GetMapping("/{source}/{roundId}/summary")
    fun summary(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("source") source: String,
        model: ModelMap,
        authentication: Authentication?,
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

    @PostMapping("/{source}/{roundId}/remind")
    fun sendReminders(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("source") source: String,
        model: RedirectAttributes
    ): String {
        feedbackRoundService.sendRemindersForRound(roundId)
        model.addFlashAttribute("message", "Sent reminders.")
        val page = if (source == "proxy") {
            "proxy"
        } else {
            "rounds"
        }
        return "redirect:/my/$page#round-$roundId"
    }

    @GetMapping("/{source}/{roundId}/responses/{responseId}")
    fun index(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("responseId") responseId: UUID,
        @PathVariable("source") source: String,
        authentication: Authentication?,
        model: ModelMap,
    ): String {
        require(null != authentication && authentication.isAuthenticated) { "user has to be logged in" }
        val response = service.findByResponseId(authentication.name, responseId)
        model.addAttribute("roundId", roundId)
        model.addAttribute("response", response)
        model.addAttribute("source", source)
        model.addAttribute("loggedInUserMail", authentication.name)
        return "my/response_detail"
    }

    data class NewMessageDto(
        @field:Size(min = 5)
        val responseMessage: String? = null
    )

    @PostMapping("/{source}/{roundId}/responses/{responseId}/messages")
    fun addMessage(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("responseId") responseId: UUID,
        @PathVariable("source") source: String,
        @ModelAttribute @Valid dto: NewMessageDto,
        authentication: Authentication?,
        model: RedirectAttributes,
    ): RedirectView {
        require(null != authentication && authentication.isAuthenticated) { "user has to be logged in" }
        service.addMessage(authentication.name, responseId, dto.responseMessage!!)
        model.addAttribute("roundId", roundId)
        model.addAttribute("source", source)
        return RedirectView("/my/$source/$roundId/responses/$responseId")
    }

}