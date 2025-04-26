package dev.mbo.t60f.domain.round

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

interface FeedbackRoundRepository : JpaRepository<FeedbackRound, UUID> {

    fun findByValidityIsBeforeAndSummaryMailedIsFalse(ts: Instant): List<FeedbackRound>

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
            left join fetch r.givers g
            left join fetch g.messages
        where r.id=:feedbackRoundId
        order by r.createdAt desc
        """
    )
    fun findByIdWithResponsesAndMessages(@Param("feedbackRoundId") feedbackRoundId: UUID): FeedbackRound?

    @Query(
        """
        select r from FeedbackRound r
            join fetch r.givers
            where r.receiver.email=:email and r.proxyReceiver is null
            order by r.createdAt desc
        """
    )
    fun findAllByReceiverEmail(email: String): List<FeedbackRound>

    @Query(
        """
        select r from FeedbackRound r
            join fetch r.givers
            where r.proxyReceiver.email=:email
            order by r.createdAt desc
        """
    )
    fun findAllByProxyEmail(email: String): List<FeedbackRound>

}