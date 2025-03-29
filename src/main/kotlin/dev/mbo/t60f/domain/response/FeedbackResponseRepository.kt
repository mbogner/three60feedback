package dev.mbo.t60f.domain.response

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface FeedbackResponseRepository : JpaRepository<FeedbackResponse, UUID> {

    fun findAllBySentAtIsNull(): List<FeedbackResponse>

    @Query(
        """
        SELECT f
        FROM FeedbackResponse f 
        WHERE f.notifiedAt is null
            AND f.negativeFeedback is not null
            AND f.positiveFeedback is not null
        """
    )
    fun findOpenResponses(): List<FeedbackResponse>

    fun findByIdAndFeedbackRoundId(id: UUID, roundId: UUID): FeedbackResponse?

}