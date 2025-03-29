package dev.mbo.t60f.domain.admin.sync

import dev.mbo.t60f.domain.company.CompanyRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class UserSync(
    private val companyRepository: CompanyRepository,
    private val syncService: SyncService
) {

    @Scheduled(cron = "0 15 3 * * *")
    internal fun syncAll() {
        companyRepository.findAll().forEach {
            syncService.sync(it.id!!)
        }
    }
}