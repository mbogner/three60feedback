package dev.mbo.t60f.domain.company

import dev.mbo.springkotlinjpa.converter.StringSetConverter
import dev.mbo.t60f.domain.request.FeedbackRequest
import dev.mbo.t60f.domain.user.User
import dev.mbo.t60f.global.jpa.AbstractEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "companies", uniqueConstraints = [UniqueConstraint(name = "companies_uc__name", columnNames = ["name"])])
class Company(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:NotBlank
    @field:Column(name = "name", length = 255, nullable = false)
    var name: String? = null,

    @field:Convert(converter = StringSetConverter::class)
    var domains: Set<String> = emptySet(),

    @field:Enumerated(EnumType.STRING)
    @field:Column(name = "sync_type", length = 32, nullable = false)
    var syncType: SyncType = SyncType.MITE,
    var syncTime: Instant? = null,

    @field:NotBlank
    @field:Column(name = "sync_base_url", nullable = false)
    var syncBaseUrl: String,

    @field:NotBlank
    @field:Column(name = "sync_api_key", nullable = false)
    var syncApiKey: String,

    @field:OneToMany(
        mappedBy = "company",
        fetch = FetchType.LAZY,
        cascade = [(CascadeType.ALL)],
        orphanRemoval = true
    )
    var users: MutableList<User> = mutableListOf(),

    @field:OneToMany(
        mappedBy = "company",
        fetch = FetchType.LAZY,
        cascade = [(CascadeType.ALL)],
        orphanRemoval = true
    )
    var feedbackRequests: MutableList<FeedbackRequest> = mutableListOf()

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

    fun updateSyncTime() {
        this.syncTime = Instant.now()
    }

    override fun toStringAttributes(): Map<String, Any?> {
        return super.toStringAttributes()
            .plus("name" to name)
            .plus("domains" to domains)
    }
}