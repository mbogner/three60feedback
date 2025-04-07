package dev.mbo.t60f.domain.round

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

interface FeedbackRoundRepository : JpaRepository<FeedbackRound, UUID> {

    fun findByValidityIsBefore(ts: Instant): List<FeedbackRound>

    @Modifying
    fun deleteByValidityIsBefore(ts: Instant)

    @Query(
        """
        select r from FeedbackRound r
            join fetch r.givers
            join fetch r.receiver
            join fetch r.receiver.company
            order by r.createdAt desc
        """
    )
    fun findAllWithResponses(): List<FeedbackRound>

    @Query(
        """
        select r from FeedbackRound r
            join fetch r.givers
        where r.id=:feedbackRoundId
            order by r.createdAt desc
        """
    )
    fun findByIdWithResponses(@Param("feedbackRoundId") feedbackRoundId: UUID): FeedbackRound?

}