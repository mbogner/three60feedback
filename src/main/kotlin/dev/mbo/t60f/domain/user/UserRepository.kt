package dev.mbo.t60f.domain.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {

    fun findAllByCompanyId(companyId: UUID, pageable: Pageable): Page<User>

    fun findByEmail(email: String): User?

    fun deleteByCompanyIdAndEmailNotIn(companyId: UUID, emails: Collection<String>)

    fun findByForgotPasswordToken(token: UUID): User?

}