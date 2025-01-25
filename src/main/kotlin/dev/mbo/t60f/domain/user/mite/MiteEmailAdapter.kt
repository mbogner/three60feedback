package dev.mbo.t60f.domain.user.mite

import dev.mbo.t60f.domain.user.adapter.Email
import dev.mbo.t60f.domain.user.adapter.EmailAdapter
import dev.mbo.t60f.logger
import jakarta.validation.Validator
import org.springframework.stereotype.Component

@Component
class MiteEmailAdapter(
    private val miteClient: MiteClient,
    validator: Validator,
) : EmailAdapter(validator) {

    private val log = logger()

    override fun retrieve(): Set<Email> {
        val miteUsers = miteClient.retrieveActiveUsers()
        log.info("retrieved ${miteUsers.size} active users from mite")
        val mails = miteUsers.map { Email(it.user.email) }.toSet()
        return validated(mails)
    }

}