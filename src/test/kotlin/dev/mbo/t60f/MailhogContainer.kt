package dev.mbo.t60f

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

class MailhogContainer : GenericContainer<MailhogContainer>("mailhog/mailhog") {
    companion object {
        private const val PORT_SMTP = 1025
        private const val PORT_HTTP = 8025
    }

    init {
        withExposedPorts(PORT_SMTP, PORT_HTTP)
        waitingFor(Wait.forLogMessage(".*Serving under.*$PORT_HTTP.*", 1))
    }

    val smtpPort get() = getMappedPort(PORT_SMTP)
    val smtpHost get() = host

    @Suppress("HttpUrlsUsage")
    val httpUrl get() = "http://$host:${getMappedPort(PORT_HTTP)}/api/v2"

}