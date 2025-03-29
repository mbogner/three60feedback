package dev.mbo.t60f

object MailpitContainerFactory {
    val instance by lazy {
        MailpitContainer().apply { start() }
    }
}