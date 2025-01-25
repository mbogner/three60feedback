package dev.mbo.t60f.domain.user

import dev.mbo.t60f.domain.company.Company
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.Column
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
    var company: Company? = null

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

    override fun toStringAttributes(): Map<String, Any?> {
        return super.toStringAttributes()
            .plus("email" to email)
            .plus("company.id" to company?.id)
    }
}