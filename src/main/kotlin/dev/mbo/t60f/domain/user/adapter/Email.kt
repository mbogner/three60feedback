package dev.mbo.t60f.domain.user.adapter

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@JvmInline
value class Email(
    @field:Email
    @field:NotBlank
    val value: String
)