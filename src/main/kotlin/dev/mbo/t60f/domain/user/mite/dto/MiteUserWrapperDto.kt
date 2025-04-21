package dev.mbo.t60f.domain.user.mite.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MiteUserWrapperDto(
    @JsonProperty("user")
    val user: MiteUserDto
)