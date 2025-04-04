package dev.mbo.t60f.domain.user

import dev.mbo.t60f.domain.company.Company
import dev.mbo.t60f.domain.user.converter.RoleListConverter
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(name = "users_uc__email", columnNames = ["email"])])
class User(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:NotBlank
    @field:Column(name = "email", nullable = false)
    var email: String,

    @field:NotNull
    @field:ManyToOne(optional = false)
    @field:JoinColumn(name = "company_id", nullable = false)
    var company: Company? = null,

    var passwordHash: String? = null,
    var forgotPasswordToken: UUID? = null,
    var forgotPasswordCreatedAt: Instant? = null,
    var forgotPasswordSentAt: Instant? = null,
    var forgotPasswordSendAttempts: Int = 0,

    @field:Convert(converter = RoleListConverter::class)
    var roles: MutableList<Role> = mutableListOf(Role.USER)

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

    fun forgotPasswordTokenCreate(): UUID {
        forgotPasswordToken = UUID.randomUUID()
        forgotPasswordCreatedAt = Instant.now()
        forgotPasswordSentAt = null
        forgotPasswordSendAttempts = 0
        return forgotPasswordToken!!
    }

    fun forgotPasswordSendSucceeded() {
        forgotPasswordSentAt = Instant.now()
    }

    fun forgotPasswordSendFailed() {
        forgotPasswordSentAt = null
        forgotPasswordSendAttempts += 1
    }

    fun resetPassword(hash: String) {
        forgotPasswordToken = null
        forgotPasswordCreatedAt = null
        forgotPasswordSentAt = null
        forgotPasswordSendAttempts = 0
        passwordHash = hash
    }

    override fun toStringAttributes(): Map<String, Any?> {
        return super.toStringAttributes()
            .plus("email" to email)
            .plus("company.id" to company?.id)
    }
}