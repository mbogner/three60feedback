package dev.mbo.t60f.domain.auth

import dev.mbo.logging.logger
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
class AuthController(
    private val forgotPasswordService: ForgotPasswordService,
) {

    private val log = logger()

    @GetMapping("/login")
    fun loginPage(): String {
        return "auth/login"
    }

    @GetMapping("/forgot-password")
    fun forgotPassword(): String {
        return "auth/forgot_password"
    }

    @PostMapping("/forgot-password")
    fun forgotPasswordSend(
        @RequestParam("username") username: String
    ): RedirectView {
        log.debug("forgot password request: {}", username)
        forgotPasswordService.send(username)
        return RedirectView("/")
    }

    @GetMapping("/reset-password")
    fun resetPassword(): String {
        return "auth/reset_password"
    }

    @PostMapping("/reset-password")
    fun resetPasswordExecute(
        @RequestParam token: UUID,
        @RequestParam password: String,
        @RequestParam confirmPassword: String
    ): RedirectView {
        if (password != confirmPassword) {
            log.debug("invalid reset password request")
            return RedirectView("/reset-password?token=$token&error=true")
        }
        forgotPasswordService.reset(token, password)
        return RedirectView("/login?reset=true")
    }

}