package dev.mbo.t60f.views.admin

import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.domain.round.FeedbackRoundRepository
import dev.mbo.t60f.domain.round.FeedbackRoundService
import dev.mbo.t60f.domain.round.FeedbackRoundSummaryService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.UUID

@Controller
@RequestMapping("/admin/rounds")
class AdminRoundController(
    private val feedbackRoundRepository: FeedbackRoundRepository,
    private val feedbackResponseRepository: FeedbackResponseRepository,
    private val feedbackRoundService: FeedbackRoundService,
    private val summaryService: FeedbackRoundSummaryService,
) {

    @GetMapping
    fun rounds(model: ModelMap): String {
        val rounds = feedbackRoundRepository.findAllWithResponses()
        rounds.forEach {
            it.givers.sortedBy { response -> response.email }
        }
        model.addAttribute("rounds", rounds)
        return "admin/rounds"
    }

    @GetMapping("/{roundId}/responses/{responseId}")
    fun giver(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("responseId") responseId: UUID,
        model: ModelMap,
        authentication: Authentication?
    ): String {
        require(null != authentication && authentication.isAuthenticated) { "user has to be logged in" }
        val response = feedbackResponseRepository.findByIdAndFeedbackRoundId(responseId, roundId)
            ?: throw EntityNotFoundException("no giver for round $roundId with id $responseId found")
        model.addAttribute("response", response)
        model.addAttribute("loggedInUserMail", authentication.name)
        return "admin/response"
    }

    @Transactional
    @GetMapping("/{roundId}/summary")
    fun summary(
        @PathVariable("roundId") roundId: UUID,
        model: ModelMap
    ): String {
        val summary = summaryService.createSummary(roundId)
        model.addAttribute("summary", summary)
        return "admin/feedback_summary"
    }

    @Transactional
    @PostMapping("/{roundId}/delete")
    fun deleteRound(
        @PathVariable("roundId") roundId: UUID,
        model: RedirectAttributes
    ): String {
        feedbackRoundRepository.deleteById(roundId)
        model.addFlashAttribute("message", "Round deleted successfully.")
        return "redirect:/admin/rounds#round-$roundId"
    }

    @Transactional
    @PostMapping("/{roundId}/validity/extend")
    fun validityExtend(
        @PathVariable("roundId") roundId: UUID,
        model: RedirectAttributes
    ): String {
        feedbackRoundService.extendValidity(roundId)
        model.addFlashAttribute("message", "Round extended for 1 day successfully.")
        return "redirect:/admin/rounds#round-$roundId"
    }

    @Transactional
    @PostMapping("/{roundId}/validity/shorten")
    fun validityShorten(
        @PathVariable("roundId") roundId: UUID,
        model: RedirectAttributes
    ): String {
        feedbackRoundService.shortenValidity(roundId)
        model.addFlashAttribute("message", "Round shortened for 1 day successfully.")
        return "redirect:/admin/rounds#round-$roundId"
    }

    @Transactional
    @PostMapping("/{roundId}/reopen")
    fun reopenRound(
        @PathVariable("roundId") roundId: UUID,
        model: RedirectAttributes
    ): String {
        feedbackRoundService.reopen(roundId)
        model.addFlashAttribute("message", "Round reopened successfully.")
        return "redirect:/admin/rounds#round-$roundId"
    }

    @Transactional
    @PostMapping("/{roundId}/end")
    fun endRound(
        @PathVariable("roundId") roundId: UUID,
        model: RedirectAttributes
    ): String {
        feedbackRoundService.end(roundId)
        model.addFlashAttribute("message", "Round set to end now.")
        return "redirect:/admin/rounds#round-$roundId"
    }

}