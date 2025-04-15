package dev.mbo.t60f.domain.my

import dev.mbo.t60f.domain.response.message.FeedbackResponseMessageService
import jakarta.validation.constraints.Size
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID
import javax.validation.Valid

@Controller
@RequestMapping("/my/response/{responseId}/questions")
class MyFeedbackResponseMessageController(
    private val service: FeedbackResponseMessageService,
) {

    @GetMapping
    fun index(
        @PathVariable("responseId") responseId: UUID,
        @RequestParam(value = "source", required = false) source: String? = null,
        authentication: Authentication?,
        model: ModelMap,
    ): String {
        require(null != authentication && authentication.isAuthenticated) { "user has to be logged in" }
        val response = service.findByResponseId(authentication.name, responseId)
        model.addAttribute("response", response)
        model.addAttribute("source", source)
        model.addAttribute("loggedInUserMail", authentication.name)
        return "my/response_detail"
    }

    data class NewMessageDto(
        @field:Size(min = 5)
        val responseMessage: String? = null
    )

    @PostMapping
    fun addMessage(
        @PathVariable("responseId") responseId: UUID,
        @RequestParam(value = "source", required = false) source: String? = null,
        @ModelAttribute @Valid dto: NewMessageDto,
        authentication: Authentication?,
        model: RedirectAttributes,
    ): RedirectView {
        require(null != authentication && authentication.isAuthenticated) { "user has to be logged in" }
        service.addMessage(authentication.name, responseId, dto.responseMessage!!)
        model.addAttribute("source", source)
        return RedirectView("/my/response/$responseId/questions")
    }

}