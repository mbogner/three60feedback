package dev.mbo.t60f.domain.response.message

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FeedbackResponseMessageRepository : JpaRepository<FeedbackResponseMessage, UUID> {

    fun findByMessageSentAtIsNull(): List<FeedbackResponseMessage>

}