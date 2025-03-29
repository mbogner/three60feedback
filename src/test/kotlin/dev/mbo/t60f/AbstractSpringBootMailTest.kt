package dev.mbo.t60f

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container

abstract class AbstractSpringBootMailTest : AbstractSpringBootTest() {

    @Container
    protected val mailpit = MailpitContainer()

    protected var spec: RequestSpecification? = null

    @BeforeEach
    fun before() {
        if (!mailpit.isRunning) {
            mailpit.start()
            spec = RequestSpecBuilder().setBaseUri(mailpit.httpUrl).build()
        }
    }

    @Test
    fun checkMailConfig() {
        assertThat(mailpit.httpUrl).isNotBlank()
        assertThat(mailpit.smtpHost).isNotBlank()
        assertThat(mailpit.smtpPort).isNotNull()
    }

}