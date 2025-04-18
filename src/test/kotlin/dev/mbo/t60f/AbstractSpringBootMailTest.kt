package dev.mbo.t60f

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container

abstract class AbstractSpringBootMailTest : AbstractSpringBootTest() {

    @Container
    protected val mailpit = MailpitContainerFactory.instance

    protected var mailpitApiSpec: RequestSpecification? = null

    @BeforeEach
    fun before() {
        synchronized(this) {
            if (!mailpit.isRunning) {
                mailpit.start()
            }
            if (null == mailpitApiSpec) {
                mailpitApiSpec = RequestSpecBuilder().setBaseUri(mailpit.httpApiUrl).build()
            }
        }
    }

    @Test
    fun checkMailConfig() {
        assertThat(mailpit.httpApiUrl).isNotBlank()
        assertThat(mailpit.smtpHost).isNotBlank()
        assertThat(mailpit.smtpPort).isNotNull()
    }

    companion object {
        private val mailpit = MailpitContainerFactory.instance

        @JvmStatic
        @DynamicPropertySource
        fun registerMailpitProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.mail.host") { mailpit.smtpHost }
            registry.add("spring.mail.port") { mailpit.smtpPort }
            registry.add("spring.mail.properties.mail.smtp.auth") { "false" }
        }
    }

}