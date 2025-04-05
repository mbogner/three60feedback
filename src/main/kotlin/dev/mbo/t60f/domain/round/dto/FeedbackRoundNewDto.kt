package dev.mbo.t60f.domain.round.dto

data class FeedbackRoundNewDto(
    val proxy: String?,
    val receiver: String,
    val invites: List<String> = emptyList(),
    val days: Int,
    val focus: String?,
)
