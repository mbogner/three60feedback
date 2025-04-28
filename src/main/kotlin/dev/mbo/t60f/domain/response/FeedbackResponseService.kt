package dev.mbo.t60f.domain.response

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Service
class FeedbackResponseService(
    private val repository: FeedbackResponseRepository
) {

    fun findById(id: UUID): FeedbackResponse {
        return repository.findById(id).orElseThrow()
    }

    @Transactional
    fun updateReported(id: UUID, reported: Boolean) {
        val giver = repository.findById(id).orElseThrow()
        giver.reported = reported
        repository.save(giver)
    }

    @Transactional
    fun store(id: UUID, positive: String, negative: String): UUID {
        val response = repository.findById(id).orElseThrow()
        if (null != response.positiveFeedback || null != response.negativeFeedback) {
            throw IllegalStateException("Feedback already given")
        }
        if(response.feedbackRound.summaryMailed) {
            throw IllegalStateException("Feedback round already closed")
        }
        response.positiveFeedback = positive
        response.negativeFeedback = negative

        return response.feedbackRound.receiver.company!!.id!!
    }

}