package dev.mbo.t60f.domain.round

import dev.mbo.t60f.domain.request.FeedbackRequestService
import dev.mbo.t60f.domain.round.dto.FeedbackRoundNewDto
import dev.mbo.t60f.logger
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
@RequestMapping("/rounds")
class FeedbackRoundController(
    private val service: FeedbackRoundService,
    private val feedbackRequestService: FeedbackRequestService,
) {

    private val log = logger()

    @PostMapping(path = ["/create", "/create/"])
    fun createFeedbackRound(
        @ModelAttribute
        @Valid
        dto: FeedbackRoundNewDto,
        @RequestParam(required = true)
        requestId: UUID,
        redirectAttrs: RedirectAttributes
    ): RedirectView {
        log.info("create feedback round for {}", dto)
        val request = feedbackRequestService.findById(requestId)
        service.create(request, dto.receiver, dto.invites)
        redirectAttrs.addFlashAttribute(
            "message",
            "Created feedback round for ${dto.receiver}. Requests are sent immediately via e-mail."
        )
        return RedirectView("/requests")
    }

}