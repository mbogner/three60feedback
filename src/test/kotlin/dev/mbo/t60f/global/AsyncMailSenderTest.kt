package dev.mbo.t60f.global

import dev.mbo.t60f.AbstractSpringBootMailTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AsyncMailSenderTest @Autowired constructor(
    val sender: AsyncMailSender,
) : AbstractSpringBootMailTest() {

    @Test
    fun send() {
        sender.send("test@gmail.com", "subject", "content")
    }

}