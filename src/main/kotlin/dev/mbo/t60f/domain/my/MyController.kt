package dev.mbo.t60f.domain.my

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/my")
class MyController {

    @GetMapping
    fun index(): RedirectView {
        return RedirectView("/my/rounds")
    }

}