package dev.mbo.t60f.domain.user.mite.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class MiteUserDto(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("created_at")
    val createdAt: Instant,
    @JsonProperty("updated_at")
    val updatedAt: Instant,
    @JsonProperty("archived")
    val archived: Boolean,
    @JsonProperty("language")
    val language: String,
    @JsonProperty("role")
    val role: String,
)