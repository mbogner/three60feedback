package dev.mbo.t60f.domain.user.sync

import dev.mbo.kotlinencryption.Encryptor
import dev.mbo.logging.logger
import dev.mbo.t60f.domain.company.Company
import dev.mbo.t60f.domain.company.CompanyRepository
import dev.mbo.t60f.domain.company.SyncType
import dev.mbo.t60f.domain.user.User
import dev.mbo.t60f.domain.user.UserRepository
import dev.mbo.t60f.domain.user.adapter.EmailAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.util.UUID
import javax.crypto.BadPaddingException

@Service
class UserSyncService(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val encryptor: Encryptor,
    emailAdapters: List<EmailAdapter>,
    @Value("\${app.user.minSyncDiff:15}") private val syncDiff: Long,
) {

    private val log = logger()
    private val comparisonDelay = Duration.ofMinutes(syncDiff)
    private val adapterMap: Map<SyncType, EmailAdapter> = emailAdapters.associateBy { it.validFor() }

    @Transactional
    fun sync(companyId: UUID) {
        val company = retrieveCompany(companyId)
        assertMinSyncTime(company.syncTime)
        val adapter = chooseEmailAdapter(company)
        val decryptedApiKey = try {
            encryptor.decrypt(company.syncApiKey)
        } catch (exc: BadPaddingException) {
            throw IllegalStateException("failed to decrypt api key", exc)
        }
        val retrievedMails = adapter.retrieve(company.syncBaseUrl, decryptedApiKey).map { it.value.lowercase() }.toSet()
        updateUsers(company, retrievedMails)
        updateCompany(company)
    }

    private fun updateCompany(company: Company) {
        company.updateSyncTime()
        companyRepository.save(company)
    }

    private fun updateUsers(company: Company, retrievedMails: Collection<String>) {
        userRepository.deleteByCompanyIdAndEmailNotIn(company.id!!, retrievedMails)
        retrievedMails.forEach { mail ->
            val domain = mail.substring(mail.indexOf("@") + 1)
            if (company.domains.contains(domain)) {
                val user = userRepository.findByEmail(mail)
                if (user == null) { // user wasn't found and can be added
                    userRepository.save(User(email = mail, company = company))
                } else { // user was found so the mail already exists
                    if (user.company != company) { // log warning if the user is within another company
                        log.warn("user {} already exists within another company: {}", mail, user.company?.name)
                    }
                }
            } else {
                log.warn("mail {} not in domain list of company {}", mail, company.name)
            }
        }
    }

    private fun chooseEmailAdapter(company: Company): EmailAdapter {
        return adapterMap[company.syncType]
            ?: throw IllegalArgumentException("no sync adapter (${company.syncType}) for company ${company.name}")
    }

    private fun retrieveCompany(companyId: UUID): Company {
        return companyRepository.findByIdOrNull(companyId)
            ?: throw IllegalArgumentException("no company with id $companyId")
    }

    private fun assertMinSyncTime(syncTime: Instant?, ts: Instant = Instant.now()) {
        if (null != syncTime) {
            val passedDelay = Duration.between(syncTime, ts)
            if (passedDelay < comparisonDelay)
                throw IllegalStateException("you can only sync every $syncDiff minutes")
        }
    }

}