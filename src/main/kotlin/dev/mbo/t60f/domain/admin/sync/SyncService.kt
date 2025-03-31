package dev.mbo.t60f.domain.admin.sync

import dev.mbo.kotlinencryption.Encryptor
import dev.mbo.t60f.domain.company.CompanyRepository
import dev.mbo.t60f.domain.user.User
import dev.mbo.t60f.domain.user.UserRepository
import dev.mbo.t60f.domain.user.adapter.EmailAdapter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SyncService(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val emailAdapters: List<EmailAdapter>,
    private val encryptor: Encryptor,
) {

    @Transactional
    fun sync(companyId: UUID) {
        val company = companyRepository.findByIdOrNull(companyId)
            ?: throw IllegalArgumentException("no company with id $companyId")

        val retrievedMails = emailAdapters
            .flatMap { it.retrieve(company.miteBaseUrl, encryptor.decrypt(company.miteApiKey)) }
            .map { it.value }
            .toSet()
        userRepository.deleteByCompanyIdAndEmailNotIn(companyId, retrievedMails)
        retrievedMails.forEach { mail ->
            val domain = mail.substring(mail.indexOf("@") + 1)
            if (company.domains.contains(domain)
                && userRepository.findByEmail(mail) == null
            ) {
                userRepository.save(User(email = mail, company = companyRepository.getReferenceById(companyId)))
            }
        }
    }

}