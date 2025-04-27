package dev.mbo.t60f.views.admin

import dev.mbo.t60f.domain.user.Role
import dev.mbo.t60f.domain.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
@RequestMapping("/admin/companies/{companyId}/users")
class AdminCompanyUserController(
    private val userService: UserService
) {

    @GetMapping
    fun listUsers(
        @PathVariable companyId: UUID,
        model: Model
    ): String {
        val users = userService.findAllByCompanyId(companyId)
        model.addAttribute("users", users)
        model.addAttribute("companyId", companyId)
        return "admin/company_users"
    }

    @PostMapping("/update-roles")
    fun updateRoles(
        @RequestParam userId: UUID,
        @RequestParam roles: List<Role>,
        @PathVariable companyId: UUID,
        redirectAttributes: RedirectAttributes
    ): RedirectView {
        val user = userService.updateUserRoles(userId, roles.toSet())
        redirectAttributes.addFlashAttribute("message", "Roles updated for user ${user.email} to ${user.roles}")
        return RedirectView("/admin/companies/$companyId/users")
    }
}