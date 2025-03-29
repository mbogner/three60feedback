package dev.mbo.t60f.domain.response

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.response.dto.FeedbackDto
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/response")
class FeedbackResponseController(
    private val feedbackResponseService: FeedbackResponseService,
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
            feedbackResponseService.findById(id)
        )
        return "feedback"
    }

    @Transactional
    @GetMapping(path = ["/{id}/report", "/{id}/report/"])
    fun report(
        @PathVariable(
            "id",
            required = true
        )
        id: UUID,
        model: Model
    ): String {
        feedbackResponseService.updateReported(id, true)
        model.addAttribute("message", "feedback has been reported")
        return "sent"
    }

    @Transactional
    @GetMapping(path = ["/{id}/unreport", "/{id}/unreport/"])
    fun unreport(
        @PathVariable(
            "id",
            required = true
        )
        id: UUID,
        model: Model
    ): String {
        feedbackResponseService.updateReported(id, false)
        model.addAttribute("message", "report of feedback has been removed")
        return "sent"
    }

    @PostMapping(path = ["/{id}/send", "/{id}/send"])
    fun send(
        @PathVariable(
            "id",
            required = true
        )
        id: UUID,
        @ModelAttribute
        @Valid
        dto: FeedbackDto,
        model: ModelMap
    ): String {
        log.debug("received feedback from giver {}: {}", id, dto)
        val companyId = feedbackResponseService.store(id = id, positive = dto.positive, negative = dto.negative)
        model.addAttribute(
            "message",
            "Handed in feedback. Thank you. It will be sent to the receiver immediately."
        )
        model.addAttribute("companyId", companyId)
        return "sent"
    }

}