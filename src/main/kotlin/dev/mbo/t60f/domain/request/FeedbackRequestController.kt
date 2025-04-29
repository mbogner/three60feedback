package dev.mbo.t60f.domain.request

import dev.mbo.logging.logger
import dev.mbo.springkotlinweb.CookieManager
import dev.mbo.t60f.domain.request.dto.FeedbackRequestNewDto
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
import java.util.UUID

@Controller
@RequestMapping("/requests")
class FeedbackRequestController(
    private val service: FeedbackRequestService
) {

    companion object {
        const val COOKIE_COMPANY_ID = "companyId"
    }

    private val log = logger()

    @GetMapping(path = ["", "/"])
    fun requests(
        @RequestParam(required = false) companyId: UUID?,
        model: Model,
        request: HttpServletRequest
    ): String {
        val cid = CookieManager.retrieve(companyId, request, COOKIE_COMPANY_ID, CookieManager.uuidParser)
        model.addAttribute("companyId", cid)
        return "requests"
    }

    @PostMapping(path = ["", "/"])
    fun create(
        @Valid dto: FeedbackRequestNewDto,
        model: ModelMap,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): String {
        log.debug("request: {}", dto)
        require(dto.email.trim().isNotBlank()) { "email must not be blank" }
        service.create(dto)
        model.addAttribute("message", "Sent token to ${dto.email}")
        model.addAttribute("companyId", dto.companyId)
        CookieManager.update(
            value = dto.companyId,
            request = request,
            response = response,
            cookieName = COOKIE_COMPANY_ID,
            serializer = CookieManager.uuidSerializer
        )
        return "sent"
    }

}