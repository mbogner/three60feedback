package dev.mbo.t60f.domain.request.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.*

data class FeedbackRequestNewDto(
    @NotNull
    var companyId: UUID?,
    @NotEmpty
    @Email
    var email: String = ""
)