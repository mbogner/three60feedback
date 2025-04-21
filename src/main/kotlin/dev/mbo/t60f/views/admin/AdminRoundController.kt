package dev.mbo.t60f.views.admin

import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.domain.round.FeedbackRoundRepository
import dev.mbo.t60f.domain.round.FeedbackRoundSummaryService
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@Controller
@RequestMapping("/admin/rounds")
class AdminRoundController(
    private val feedbackRoundRepository: FeedbackRoundRepository,
    private val feedbackResponseRepository: FeedbackResponseRepository,
    private val summaryService: FeedbackRoundSummaryService,
) {

    @GetMapping
    fun rounds(model: ModelMap): String {
        val rounds = feedbackRoundRepository.findAllWithResponses()
        rounds.forEach {
            it.givers.sortedBy { it.email }
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
        val round = feedbackRoundRepository.findByIdOrNull(roundId)
            ?: throw EntityNotFoundException("no round with id $roundId found")
        val summary = summaryService.createSummary(roundId)
        model.addAttribute("round", round)
        model.addAttribute("summary", summary)
        return "admin/feedback_summary"
    }

}