package dev.mbo.t60f.domain.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserNewDto(
    @field:NotBlank
    @field:Email
    val email: String,
)
