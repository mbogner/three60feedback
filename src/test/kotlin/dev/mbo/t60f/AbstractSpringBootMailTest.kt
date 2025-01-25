package dev.mbo.t60f

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container

abstract class AbstractSpringBootMailTest : AbstractSpringBootTest() {

    @Container
    protected val mailhog = MailhogContainer()

    protected var spec: RequestSpecification? = null

    @BeforeEach
    fun before() {
        if (!mailhog.isRunning) {
            mailhog.start()
            spec = RequestSpecBuilder().setBaseUri(mailhog.httpUrl).build()
        }
    }

    @Test
    fun checkMailConfig() {
        assertThat(mailhog.httpUrl).isNotBlank()
        assertThat(mailhog.smtpHost).isNotBlank()
        assertThat(mailhog.smtpPort).isNotNull()
    }

}