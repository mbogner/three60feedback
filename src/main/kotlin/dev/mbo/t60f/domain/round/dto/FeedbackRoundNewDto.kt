package dev.mbo.t60f.domain.round.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class FeedbackRoundNewDto(
    @field:NotNull
    val receiver: String,

    @field:NotNull
    @field:NotEmpty
    @field:Size(min = 5)
    val invites: List<String>,
)
