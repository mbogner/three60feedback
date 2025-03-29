package dev.mbo.t60f.domain.response.dto

import jakarta.validation.constraints.NotBlank

data class FeedbackDto(
    @field:NotBlank
    val positive: String,

    @field:NotBlank
    val negative: String,
)