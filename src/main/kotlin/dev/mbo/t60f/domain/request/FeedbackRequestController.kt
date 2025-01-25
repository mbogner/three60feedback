package dev.mbo.t60f.domain.request

import dev.mbo.t60f.logger
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/requests")
class FeedbackRequestController(
    private val service: FeedbackRequestService
) {

    private val log = logger()

    @GetMapping(path = ["", "/"])
    fun companies(): String {
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
        return "request"
    }

}