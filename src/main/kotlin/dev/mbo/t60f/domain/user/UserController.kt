package dev.mbo.t60f.domain.user

import dev.mbo.t60f.domain.request.FeedbackRequestService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val feedbackRequestService: FeedbackRequestService,
) {

    @GetMapping(path = ["", "/"])
    fun getUsers(
        @RequestParam(required = true) requestId: UUID,
        @RequestParam(required = false) override: Boolean = false,
        model: Model
    ): String {
        val request = feedbackRequestService.findById(requestId)
        model.addAttribute(
            "requestId", requestId
        )
        model.addAttribute(
            "override", override
        )
        model.addAttribute(
            "users", if (override) {
                userService.findAllByCompanyId(request.company!!.id!!)
            } else {
                listOf(userService.findByEmail(request.email!!))
            }

        )
        return "users"
    }

    @PostMapping(path = ["/sync", "/sync/"])
    fun syncUsers(
        @RequestParam(required = true) requestId: UUID, redirectAttrs: RedirectAttributes
    ): RedirectView {
        val request = feedbackRequestService.findById(requestId)
        userService.sync(request.company!!.id!!)
        redirectAttrs.addFlashAttribute(
            "message", "Feedbackers synced"
        )
        redirectAttrs.addAttribute("requestId", requestId)
        return RedirectView("/users")
    }

    @GetMapping(path = ["/{userId}/feedback_round", "/{userId}/feedback_round/"])
    fun feedbackRound(
        @PathVariable(
            "userId", required = true
        ) userId: UUID, @RequestParam(required = true) requestId: UUID, model: Model
    ): String {
        val request = feedbackRequestService.findById(requestId)
        model.addAttribute("requestId", requestId)

        model.addAttribute("user", userService.findById(userId))
        model.addAttribute("users", userService.findAllByCompanyId(request.company!!.id!!))
        return "feedback_round"
    }

}