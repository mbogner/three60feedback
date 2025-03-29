package dev.mbo.t60f.domain.admin.rounds

import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import dev.mbo.t60f.domain.round.FeedbackRoundRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

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

    @GetMapping("/{roundId}/giver/{giverId}")
    fun giver(
        @PathVariable("roundId") roundId: UUID,
        @PathVariable("giverId") giverId: UUID,
        model: ModelMap
    ): String {
        val giver = feedbackResponseRepository.findByIdAndFeedbackRoundId(giverId, roundId)
            ?: throw EntityNotFoundException("no giver for round $roundId with id $giverId found")
        model.addAttribute("giver", giver)
        return "admin/giver"
    }

}