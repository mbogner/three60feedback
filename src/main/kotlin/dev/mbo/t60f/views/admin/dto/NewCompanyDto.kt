package dev.mbo.t60f.views.admin.dto

data class NewCompanyDto(
    val name: String,
    val domains: String,
    val miteBaseUrl: String,
    val miteApiKey: String,
)