package dev.mbo.t60f.global

import dev.mbo.t60f.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class AsyncMailSender(
    @Suppress("SpringJavaInjectionPointsAutowiringInspection") private val sender: JavaMailSender,
    @Value("\${spring.mail.from}")
    private val from: String,
) {

    private val log = logger()

    @Async
    fun send(to: String, subject: String, content: String) {
        log.warn("sending mail $subject to $to:\n$content\n")

        val mimeMessage = sender.createMimeMessage()

        val message = MimeMessageHelper(
            mimeMessage,
            false,
            StandardCharsets.UTF_8.displayName()
        )
        message.setFrom(from)
        message.setTo(to)
        message.setSubject(subject)
        message.setText(content)
        message.setPriority(3) // 1 = high, 3 = normal, 5 = low

        sender.send(mimeMessage)
    }

}