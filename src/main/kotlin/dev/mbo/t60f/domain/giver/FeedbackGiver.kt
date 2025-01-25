package dev.mbo.t60f.domain.giver

import dev.mbo.t60f.domain.round.FeedbackRound
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.*

@Entity
@Table(name = "feedback_givers")
class FeedbackGiver(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:NotNull
    @field:ManyToOne(optional = false, fetch = FetchType.LAZY)
    @field:JoinColumn(name = "feedback_round_id", nullable = false)
    var feedbackRound: FeedbackRound,

    @field:NotBlank
    @field:Column(name = "email", nullable = false)
    var email: String,

    @field:Column(name = "sent_at")
    var sentAt: Instant? = null,

    @field:NotNull
    @field:Column(name = "send_failed", nullable = false)
    var sendFailed: Boolean = false,

    @field:Column(name = "positive_feedback")
    var positiveFeedback: String? = null,

    @field:Column(name = "negative_feedback")
    var negativeFeedback: String? = null,

    @field:Column(name = "notified_at")
    var notifiedAt: Instant? = null,

    @field:NotNull
    @field:Column(name = "notify_failed", nullable = false)
    var notifyFailed: Boolean = false

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

    override fun toStringAttributes(): Map<String, Any?> {
        return super.toStringAttributes()
            .plus("round_id" to feedbackRound.id)
            .plus("email" to email)
    }
}