package dev.mbo.t60f.domain.request

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
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "feedback_requests")
class FeedbackRequest(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:NotNull
    @field:JoinColumn(name = "company_id", nullable = false)
    @field:ManyToOne(optional = false)
    var company: Company? = null,

    @field:Email
    @field:Column(nullable = false)
    var email: String? = null,
) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

}