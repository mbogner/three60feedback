package dev.mbo.t60f.domain.user

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.request.FeedbackRequest
import dev.mbo.t60f.domain.request.FeedbackRequestService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Controller
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val feedbackRequestService: FeedbackRequestService,
) {

    private val log = logger()

    @GetMapping(path = ["", "/"])
    fun getUsers(
        @RequestParam(required = true) requestId: UUID,
        model: Model,
        authentication: Authentication?,
    ): String {
        val request: FeedbackRequest = feedbackRequestService.findById(requestId)
        model.addAttribute(
            "requestId", requestId
        )

        val isCoach = authentication?.authorities?.any { it.authority == "ROLE_COACH" } == true
        log.debug("isCoach: {}", isCoach)
        if (isCoach) {
            model.addAttribute(
                "users", userService.findAllByCompanyId(request.company!!.id!!)
            )
        } else {
            model.addAttribute(
                "users", listOf(userService.findByEmailAndCompanyId(request.email!!, request.company!!.id!!))
            )
        }
        return "users"
    }

    @GetMapping(path = ["/{userId}/feedback_round", "/{userId}/feedback_round/"])
    fun feedbackRound(
        @PathVariable("userId", required = true) userId: UUID,
        @RequestParam(required = true) requestId: UUID,
        model: Model
    ): String {
        val request = feedbackRequestService.findById(requestId)
        model.addAttribute("requestId", requestId)
        model.addAttribute("user", userService.findById(userId))
        model.addAttribute("users", userService.findAllByCompanyId(request.company!!.id!!))
        return "feedback_round"
    }

}