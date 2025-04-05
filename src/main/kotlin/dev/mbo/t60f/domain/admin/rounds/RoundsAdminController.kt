package dev.mbo.t60f.domain.admin.rounds

import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.domain.round.FeedbackRoundRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@Controller
@RequestMapping("/admin/rounds")
class RoundsAdminController(
    private val feedbackRoundRepository: FeedbackRoundRepository,
    private val feedbackResponseRepository: FeedbackResponseRepository,
) {

    @GetMapping
    fun rounds(model: ModelMap): String {
        model.addAttribute("rounds", feedbackRoundRepository.findAllWithGivers())
        return "admin/rounds"
    }

    @GetMapping("/{roundId}/responses/{responseId}")
    fun giver(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("responseId") responseId: UUID,
        model: ModelMap
    ): String {
        val giver = feedbackResponseRepository.findByIdAndFeedbackRoundId(responseId, roundId)
            ?: throw EntityNotFoundException("no giver for round $roundId with id $responseId found")
        model.addAttribute("giver", giver)
        return "admin/response"
    }

}