package dev.mbo.t60f.views.my

import dev.mbo.t60f.domain.response.FeedbackResponseRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/my/open-feedback")
class MyOpenFeedbackController(
    private val feedbackResponseRepository: FeedbackResponseRepository,
) {

    @GetMapping
    fun openFeedback(
        model: ModelMap,
        authentication: Authentication?,
    ): String {
        require(null != authentication && authentication.isAuthenticated) { "user has to be logged in" }
        model.addAttribute("responses", feedbackResponseRepository.findMyOpenResponses(authentication.name))
        return "my/open_feedback"
    }

}