package dev.mbo.t60f.domain.user

import dev.mbo.t60f.domain.company.CompanyRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional(readOnly = true)
@Service
class UserService(
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository,

    @Value("\${app.auth.admin.username:admin}") private val adminUser: String,
    @Value("\${app.auth.admin.password:admin}") private val adminPass: String,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    fun findByEmailAndCompanyId(email: String, companyId: UUID): User {
        return userRepository.findByEmailAndCompanyId(email, companyId)
            ?: throw EntityNotFoundException("No user with email $email and companyId $companyId")
    }

    fun findAllByCompanyId(companyId: UUID): List<User> {
        val company = companyRepository.findByIdOrNull(companyId)
            ?: throw EntityNotFoundException("No company with id $companyId")
        return userRepository.findAllByCompanyId(
            companyId = company.id!!,
            pageable = PageRequest.of(0, Int.MAX_VALUE, Sort.by("email").ascending())
        ).content
    }

    fun findById(id: UUID): User {
        return userRepository.findById(id).get()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        if (username == adminUser) return org.springframework.security.core.userdetails.User.builder()
            .username(adminUser)
            .password(passwordEncoder.encode(adminPass))
            .roles(Role.ADMIN.toString())
            .build()
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("User not found: $username")
        return org.springframework.security.core.userdetails.User.builder()
            .username(username)
            .password(user.passwordHash)
            .roles(*user.roles.map { it.toString() }.toTypedArray())
            .build()
    }

    @Transactional
    fun updateUserRoles(userId: UUID, roles: Set<Role>): User {
        if (roles.isEmpty()) throw IllegalArgumentException("empty roles list not allowed")
        val user = userRepository.findByIdOrNull(userId) ?: throw EntityNotFoundException("No user with id $userId")
        user.roles = roles.toMutableList()
        return userRepository.save(user)
    }

}