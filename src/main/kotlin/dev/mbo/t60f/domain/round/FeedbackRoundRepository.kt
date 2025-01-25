package dev.mbo.t60f.domain.round

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*

interface FeedbackRoundRepository : JpaRepository<FeedbackRound, UUID> {

    fun findByCreatedAtIsBefore(ts: Instant): List<FeedbackRound>

}