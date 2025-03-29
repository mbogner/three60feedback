package dev.mbo.t60f.domain.request

import dev.mbo.t60f.domain.company.Company
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.util.*

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