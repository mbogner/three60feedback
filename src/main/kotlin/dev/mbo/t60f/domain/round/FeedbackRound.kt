package dev.mbo.t60f.domain.round

import dev.mbo.t60f.domain.giver.FeedbackGiver
import dev.mbo.t60f.domain.user.User
import dev.mbo.t60f.global.AbstractEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
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

    @field:OneToMany(
        mappedBy = "feedbackRound",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL]
    )
    var givers: Set<FeedbackGiver> = mutableSetOf()

) : AbstractEntity<UUID>() {

    override fun getIdentifier(): UUID? {
        return id
    }

    override fun setIdentifier(id: UUID?) {
        this.id = id
    }

}