package dev.mbo.t60f.domain.admin.companies

data class NewCompanyDto(
    val name: String,
    val domains: String,
    val miteBaseUrl: String,
    val miteApiKey: String,
)
