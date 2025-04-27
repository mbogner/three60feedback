package dev.mbo.t60f.views.admin.dto

import dev.mbo.t60f.domain.company.Company
import java.util.UUID

data class EditCompanyDto(
    var name: String,
    var domains: String,
    var miteBaseUrl: String,
    var miteApiKey: String?,
    val lockVersion: Int,
) {
    companion object {
        fun from(company: Company): EditCompanyDto {
            return EditCompanyDto(
                name = company.name!!,
                domains = company.domains.joinToString(","),
                miteBaseUrl = company.syncBaseUrl,
                miteApiKey = company.syncApiKey,
                lockVersion = company.lockVersion!!
            )
        }
    }
}