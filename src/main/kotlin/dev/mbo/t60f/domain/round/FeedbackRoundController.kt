package dev.mbo.t60f.domain.round

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.request.FeedbackRequestService
import dev.mbo.t60f.domain.round.dto.FeedbackRoundNewDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("/rounds")
class FeedbackRoundController(
    @Value("\${app.invites.min:5}") private val minInvites: Int,
    private val service: FeedbackRoundService,
    private val feedbackRequestService: FeedbackRequestService,
) {

    private val log = logger()

    @PostMapping(path = ["/create", "/create/"])
    fun createFeedbackRound(
        @ModelAttribute
        dto: FeedbackRoundNewDto,
        @RequestParam(required = true)
        requestId: UUID,
        model: ModelMap
    ): String {
        require(dto.invites.size >= minInvites) { "you need to invite at least $minInvites people" }

        log.info("create feedback round for {}", dto)
        val request = feedbackRequestService.findById(requestId)
        service.create(request, dto.receiver, dto.invites, dto.days)
        model.addAttribute("companyId", request.company!!.id!!)
        model.addAttribute(
            "message",
            "Created feedback round for ${dto.receiver}. Requests are sent immediately via e-mail."
        )
        return "sent"
    }

}