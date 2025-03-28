package dev.mbo.t60f.domain.request

import dev.mbo.logging.logger
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("/requests")
class FeedbackRequestController(
    private val service: FeedbackRequestService
) {

    private val log = logger()

    @GetMapping(path = ["", "/"])
    fun companies(
        @RequestParam(required = false) companyId: UUID?,
        model: Model
    ): String {
        if (null != companyId) {
            model.addAttribute("companyId", companyId)
        }
        return "request"
    }

    @PostMapping(path = ["", "/"])
    fun create(
        @Valid request: FeedbackRequestNewDto,
        model: Model
    ): String {
        log.debug("request: {}", request)
        service.create(request)
        model.addAttribute(
            "message",
            "Sent token to ${request.email}"
        )
        model.addAttribute("companyId", request.companyId)
        return "request"
    }

}