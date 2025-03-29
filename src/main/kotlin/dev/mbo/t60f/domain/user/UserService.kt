package dev.mbo.t60f.domain.user

import dev.mbo.t60f.domain.company.CompanyRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Service
class UserService(
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository,
) {

    fun findByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw EntityNotFoundException("No user with email $email")
    }

    fun findAllByCompanyId(companyId: UUID): List<User> {
        companyRepository.findByIdOrNull(companyId) ?: throw EntityNotFoundException("No company with id $companyId")
        return userRepository.findAllByCompanyId(
            companyId = companyId,
            pageable = PageRequest.of(0, Int.MAX_VALUE, Sort.by("email").ascending())
        ).content
    }

    fun findById(id: UUID): User {
        return userRepository.findById(id).get()
    }

}