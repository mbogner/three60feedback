package dev.mbo.t60f.views.my

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/my")
class MyIndexController {

    @GetMapping
    fun index(): RedirectView {
        return RedirectView("/my/rounds")
    }

}