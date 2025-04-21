package dev.mbo.t60f.global.auth

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.user.UserRepository
import dev.mbo.t60f.global.AsyncMailSender
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ForgotPasswordService(
    private val userRepository: UserRepository,
    private val mailer: AsyncMailSender,
    @Value("\${app.base-url}")
    private val baseUrl: String,
    private val passwordValidator: PasswordValidator,
    private val passwordEncoder: PasswordEncoder,
) {

    private val log = logger()

    @Transactional
    fun send(username: String) {
        val user = userRepository.findByEmail(username) ?: return
        try {
            sendForgotPasswordMail(user.email, user.forgotPasswordTokenCreate())
            user.forgotPasswordSendSucceeded()
            log.debug("mail sent")
        } catch (e: Exception) {
            log.error("sending mail failed", e)
            user.forgotPasswordSendFailed()
        } finally {
            userRepository.save(user)
        }
    }

    @Transactional
    fun reset(token: UUID, password: String) {
        val user = userRepository.findByForgotPasswordToken(token)
            ?: throw EntityNotFoundException("token $token not found")
        val result = passwordValidator.validate(PasswordData(password))
        if (!result.isValid) {
            val messages = passwordValidator.getMessages(result)
            throw IllegalArgumentException(messages.joinToString("\n"))
        }
        user.resetPassword(passwordEncoder.encode(password))
        userRepository.save(user)
    }

    private fun sendForgotPasswordMail(email: String, token: UUID) {
        mailer.send(
            email, "Forgot Password Link", """
Hi $email,

somebody requested a reset password link for your account. If it wasn't you simply ignore this mail.

Your link: $baseUrl/reset-password?token=$token

Yours,
t60f""".trimIndent()
        )
    }

}