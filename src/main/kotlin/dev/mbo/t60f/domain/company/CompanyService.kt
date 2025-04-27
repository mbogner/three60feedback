package dev.mbo.t60f.domain.company

import dev.mbo.kotlinencryption.Encryptor
import dev.mbo.t60f.views.admin.dto.EditCompanyDto
import jakarta.persistence.OptimisticLockException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val encryptor: Encryptor,
) {

    @Transactional
    fun updateCompany(existingCompanyId: UUID, companyUpdate: EditCompanyDto): Company {
        val company = companyRepository.findByIdOrNull(existingCompanyId)
            ?: throw IllegalArgumentException("Company with id $existingCompanyId not found")
        if (companyUpdate.lockVersion != company.lockVersion) {
            throw OptimisticLockException("company with id $existingCompanyId was edited in the meantime.")
        }

        company.name = companyUpdate.name
        company.domains = companyUpdate.domains.split(",")
            .map { it.trim() }
            .filterNot { it.isBlank() }
            .map { it.lowercase() }
            .toSet()
        company.syncBaseUrl = companyUpdate.miteBaseUrl
        if (!companyUpdate.miteApiKey.isNullOrBlank()) {
            company.syncApiKey = encryptor.encrypt(companyUpdate.miteApiKey!!)
        }
        return companyRepository.save(company)
    }

}