package dev.mbo.t60f.domain.giver.dto

import jakarta.validation.constraints.NotBlank

data class FeedbackDto(
    @field:NotBlank
    val positive: String,

    @field:NotBlank
    val negative: String,
)