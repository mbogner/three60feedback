package dev.mbo.t60f.domain.company

import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.util.*

@Entity
@Table(name = "companies", uniqueConstraints = [UniqueConstraint(name = "companies_uc__name", columnNames = ["name"])])
class Company(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:NotBlank
    @field:Column(name = "name", length = 255, nullable = false)
    var name: String? = null

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

    override fun toStringAttributes(): Map<String, Any?> {
        return super.toStringAttributes()
            .plus("name" to name)
    }
}