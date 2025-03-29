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
        val giver = repository.findById(id).orElseThrow()
        if (null != giver.positiveFeedback || null != giver.negativeFeedback) {
            throw IllegalStateException("Feedback already given")
        }
        giver.positiveFeedback = positive
        giver.negativeFeedback = negative

        return giver.feedbackRound.receiver.company!!.id!!
    }

}