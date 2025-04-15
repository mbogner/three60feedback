package dev.mbo.t60f.domain.response

import dev.mbo.t60f.domain.response.message.FeedbackResponseMessage
import dev.mbo.t60f.domain.round.FeedbackRound
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
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "feedback_responses")
class FeedbackResponse(

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
    var notifyFailed: Boolean = false,

    @field:Column(name = "reported", nullable = false)
    var reported: Boolean = false,

    @field:NotNull
    @field:OneToMany(mappedBy = "feedbackResponse", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var messages: List<FeedbackResponseMessage> = mutableListOf()

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