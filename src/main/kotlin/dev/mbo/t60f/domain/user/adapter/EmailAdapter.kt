package dev.mbo.t60f.domain.user.adapter

import dev.mbo.logging.logger
import jakarta.validation.Validator

abstract class EmailAdapter(private val validator: Validator) {

    private val log = logger()

    abstract fun retrieve(
        baseUrl: String,
        apiKey: String,
    ): Set<Email>

    protected fun validated(mails: Set<Email>): Set<Email> {
        val result = mutableSetOf<Email>()
        mails.forEach {
            val violations = validator.validate(it)
            if (violations.isEmpty()) {
                result.add(it)
            } else {
                log.warn(
                    "{} retrieved bad email {} with violations: {}",
                    javaClass.simpleName,
                    it,
                    violations.joinToString("\n")
                )
            }
        }
        return result
    }

}