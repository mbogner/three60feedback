package dev.mbo.t60f.domain.admin.sync

import dev.mbo.t60f.domain.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
@RequestMapping("/admin/sync")
class SyncController(
    private val userService: UserService,
    private val syncService: SyncService,
) {

    @GetMapping
    fun getSync(
        @RequestParam(required = true) companyId: UUID,
        model: Model
    ): String {
        model.addAttribute("users", userService.findAllByCompanyId(companyId))
        model.addAttribute("companyId", companyId)
        return "admin/sync"
    }

    @PostMapping
    fun syncUsers(
        @RequestParam(required = true) companyId: UUID,
        model: RedirectAttributes
    ): RedirectView {
        syncService.sync(companyId)
        model.addAttribute("users", userService.findAllByCompanyId(companyId))
        model.addAttribute("companyId", companyId)
        model.addFlashAttribute(
            "message", "Feedbackers synced"
        )
        return RedirectView("/admin/sync")
    }

}