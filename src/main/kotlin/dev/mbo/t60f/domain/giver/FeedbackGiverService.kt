package dev.mbo.t60f.domain.giver

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Service
class FeedbackGiverService(
    private val repository: FeedbackGiverRepository
) {

    fun findById(id: UUID): FeedbackGiver {
        return repository.findById(id).orElseThrow()
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