package dev.mbo.t60f.domain.round

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.UUID

interface FeedbackRoundRepository : JpaRepository<FeedbackRound, UUID> {

    fun findByValidityIsBefore(ts: Instant = Instant.now()): List<FeedbackRound>

    @Query(
        """
        select r from FeedbackRound r
            join fetch r.givers
            join fetch r.receiver
            join fetch r.receiver.company
            order by r.createdAt desc
        """
    )
    fun findAllWithGivers(): List<FeedbackRound>

}