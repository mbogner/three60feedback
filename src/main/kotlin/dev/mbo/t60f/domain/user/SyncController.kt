package dev.mbo.t60f.domain.user

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@Controller
@RequestMapping("/sync")
class SyncController(
    private val userService: UserService,
) {

    @GetMapping
    fun getSync(
        @RequestParam(required = true) companyId: UUID,
        model: Model
    ): String {
        model.addAttribute("users", userService.findAllByCompanyId(companyId))
        model.addAttribute("companyId", companyId)
        return "sync"
    }

    @PostMapping
    fun syncUsers(
        @RequestParam(required = true) companyId: UUID,
        model: RedirectAttributes
    ): RedirectView {
        userService.sync(companyId)
        model.addAttribute("users", userService.findAllByCompanyId(companyId))
        model.addAttribute("companyId", companyId)
        model.addFlashAttribute(
            "message", "Feedbackers synced"
        )
        return RedirectView("/sync")
    }

}