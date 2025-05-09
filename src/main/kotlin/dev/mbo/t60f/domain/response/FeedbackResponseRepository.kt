package dev.mbo.t60f.domain.response

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

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

    @Query(
        """
        SELECT f
        FROM FeedbackResponse f 
            LEFT JOIN FETCH f.messages
        WHERE f.id = :id
            AND f.feedbackRound.id = :roundId
        """
    )
    fun findByIdAndFeedbackRoundId(@Param("id") id: UUID, @Param("roundId") roundId: UUID): FeedbackResponse?

    @Query(
        """
        SELECT r
        FROM FeedbackResponse r
            LEFT JOIN FETCH r.feedbackRound f
        WHERE r.email = :email 
            AND NOT f.summaryMailed
            AND (r.positiveFeedback is NULL OR r.negativeFeedback is NULL)
        ORDER BY r.feedbackRound.validity DESC
        """
    )
    fun findMyOpenResponses(@Param("email") email: String): List<FeedbackResponse>

    @Query(
        """
        select r
        from FeedbackResponse r
            left join fetch r.messages
            join fetch r.feedbackRound
        where r.id = :responseId
        """
    )
    fun findByIdWithMessages(@Param("responseId") responseId: UUID): FeedbackResponse?
}