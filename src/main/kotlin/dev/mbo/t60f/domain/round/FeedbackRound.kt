package dev.mbo.t60f.domain.round

import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.domain.user.User
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "feedback_rounds")
class FeedbackRound(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:ManyToOne(optional = false, fetch = FetchType.EAGER)
    @field:JoinColumn(name = "receiver_id", nullable = false)
    var receiver: User,

    @field:ManyToOne(optional = true, fetch = FetchType.EAGER)
    @field:JoinColumn(name = "proxy_receiver_id", nullable = true)
    var proxyReceiver: User? = null,

    @field:OneToMany(
        mappedBy = "feedbackRound",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL]
    )
    @field:OrderBy("email ASC")
    var givers: Set<FeedbackResponse> = mutableSetOf(),

    var validity: Instant,

    var focus: String?,

    @field:Column(columnDefinition = "text")
    var summary: String? = null,
    var summaryMailed: Boolean = false,
    var summaryTs: Instant? = null

) : AbstractEntity<UUID>() {

    fun feedbackReceiver(): User {
        return if (null == proxyReceiver) receiver else proxyReceiver!!
    }

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

    fun answered(): Int {
        return givers.filter { it.positiveFeedback != null && it.negativeFeedback != null }.size
    }

}