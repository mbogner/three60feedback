package dev.mbo.t60f.domain.round.dto

data class FeedbackRoundNewDto(
    val receiver: String,
    val invites: List<String> = emptyList(),
    val days: Int,
)
