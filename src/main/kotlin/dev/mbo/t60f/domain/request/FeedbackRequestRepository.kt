package dev.mbo.t60f.domain.request

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.UUID

interface FeedbackRequestRepository : JpaRepository<FeedbackRequest, UUID> {

    fun findByCreatedAtIsBefore(ts: Instant): List<FeedbackRequest>

}