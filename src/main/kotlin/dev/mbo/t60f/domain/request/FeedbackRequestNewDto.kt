package dev.mbo.t60f.domain.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class FeedbackRequestNewDto(
    @NotNull
    val companyId: UUID,
    @NotEmpty
    @Email
    val email: String,
)
