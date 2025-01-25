package dev.mbo.t60f.domain

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@Controller
class IndexController {

    @GetMapping(path = ["", "/"])
    fun index(
        redirectAttrs: RedirectAttributes
    ): RedirectView {
        return RedirectView("/requests")
    }

}