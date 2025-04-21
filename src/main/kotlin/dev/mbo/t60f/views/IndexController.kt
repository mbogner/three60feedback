package dev.mbo.t60f.views

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
class IndexController {

    @GetMapping(path = ["", "/"])
    fun index(
        @RequestParam(required = false) companyId: UUID?,
        redirectAttrs: RedirectAttributes
    ): RedirectView {
        if (null != companyId) {
            redirectAttrs.addAttribute("companyId", companyId)
        }
        return RedirectView("/requests")
    }

}