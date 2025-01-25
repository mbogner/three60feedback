package dev.mbo.t60f.domain.giver

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface FeedbackGiverRepository : JpaRepository<FeedbackGiver, UUID> {

    fun findAllBySentAtIsNull(): List<FeedbackGiver>

    @Query(
        """
        SELECT f
        FROM FeedbackGiver f 
        WHERE f.notifiedAt is null
            AND f.negativeFeedback is not null
            AND f.positiveFeedback is not null
        """
    )
    fun findOpenResponses(): List<FeedbackGiver>

}