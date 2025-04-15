package dev.mbo.t60f.domain.response.message

import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.util.UUID
import javax.validation.constraints.NotNull

@Entity
@Table(name = "feedback_response_messages")
class FeedbackResponseMessage(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @field:NotNull
    @field:ManyToOne(optional = false, fetch = FetchType.LAZY)
    @field:JoinColumn(name = "feedback_response_id", nullable = false)
    var feedbackResponse: FeedbackResponse,

    @field:NotNull
    @field:Email
    @field:Column(name = "sender_mail", length = 255, nullable = false)
    var senderMail: String,

    @field:NotBlank
    @field:Column(columnDefinition = "text", nullable = false)
    var message: String,

    @field:Column(name = "message_sent_at")
    var messageSentAt: Instant? = null,

    @field:Column(name = "message_send_fails")
    var messageSendFails: Int = 0

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

    override fun toStringAttributes(): Map<String, Any?> {
        return super.toStringAttributes()
            .plus("senderMail" to senderMail)
            .plus("message" to message)
            .plus("messageSentAt" to messageSentAt)
            .plus("messageSendFails" to messageSendFails)
    }

}
