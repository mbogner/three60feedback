package dev.mbo.t60f.domain.user

import dev.mbo.t60f.domain.company.CompanyRepository
import dev.mbo.t60f.domain.user.adapter.EmailAdapter
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional(readOnly = true)
@Service
class UserService(
    private val repository: UserRepository,
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val emailAdapters: List<EmailAdapter>,
) {

    fun findByEmail(email: String): User {
        return repository.findByEmail(email) ?: throw EntityNotFoundException("No user with email $email")
    }

    fun findAllByCompanyId(companyId: UUID): List<User> {
        return repository.findAllByCompanyId(
            companyId = companyId,
            pageable = PageRequest.of(0, Int.MAX_VALUE, Sort.by("email").ascending())
        ).content
    }

    @Transactional
    fun sync(companyId: UUID) {
        val mails = emailAdapters.flatMap { it.retrieve() }.map { it.value }.toSet()
        repository.deleteByEmailNotIn(mails)
        mails.forEach { mail ->
            if (repository.findByEmail(mail) == null) {
                repository.save(User(email = mail, company = companyRepository.getReferenceById(companyId)))
            }
        }
    }

    fun findById(id: UUID): User {
        return userRepository.findById(id).get()
    }

}