package dev.mbo.t60f.domain.request

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.request.dto.FeedbackRequestNewDto
import dev.mbo.t60f.global.CookieManagerUUID
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
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
    fun requests(
        @RequestParam(required = false) companyId: UUID?,
        model: Model,
        request: HttpServletRequest
    ): String {
        val cid = CookieManagerUUID.retrieve(companyId, request)
        model.addAttribute("companyId", cid)
        return "requests"
    }

    @PostMapping(path = ["", "/"])
    fun create(
        @Valid dto: FeedbackRequestNewDto,
        model: ModelMap,
        response: HttpServletResponse
    ): String {
        log.debug("request: {}", dto)
        service.create(dto)
        model.addAttribute("message", "Sent token to ${dto.email}")
        model.addAttribute("companyId", dto.companyId)
        CookieManagerUUID.update(dto.companyId, response)
        return "sent"
    }

}