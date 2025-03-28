package dev.mbo.t60f.domain.giver

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.giver.dto.FeedbackDto
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
@RequestMapping("/response")
class FeedbackGiverController(
    private val feedbackGiverService: FeedbackGiverService,
) {

    private val log = logger()

    @GetMapping(path = ["/{id}", "/{id}/"])
    fun index(
        @PathVariable(
            "id",
            required = true
        )
        id: UUID,
        model: Model
    ): String {
        model.addAttribute(
            "giver",
            feedbackGiverService.findById(id)
        )
        return "feedback"
    }

    @PostMapping(path = ["/{id}/send", "/{id}/send"])
    fun createUser(
        @PathVariable(
            "id",
            required = true
        )
        id: UUID,
        @ModelAttribute
        @Valid
        dto: FeedbackDto,
        redirectAttrs: RedirectAttributes
    ): RedirectView {
        log.debug("received feedback from giver {}: {}", id, dto)
        val companyId = feedbackGiverService.store(id = id, positive = dto.positive, negative = dto.negative)
        redirectAttrs.addFlashAttribute(
            "message",
            "Handed in feedback. Thank you. It will be sent to the receiver immediately."
        )
        redirectAttrs.addAttribute("companyId", companyId)
        return RedirectView("/requests")
    }

}