package dev.mbo.t60f.domain.round

import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.domain.user.User
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "feedback_rounds")
class FeedbackRound(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:ManyToOne(optional = false, fetch = FetchType.EAGER)
    @field:JoinColumn(name = "receiver_id", nullable = false)
    var receiver: User,

    @field:OneToMany(
        mappedBy = "feedbackRound",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL]
    )
    @field:OrderBy("email ASC")
    var givers: Set<FeedbackResponse> = mutableSetOf(),

    @Suppress("unused") var validity: Instant,

    var focus: String?,

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

}