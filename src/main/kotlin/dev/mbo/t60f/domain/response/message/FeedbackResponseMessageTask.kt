package dev.mbo.t60f.domain.response.message

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class FeedbackResponseMessageTask(
    private val service: FeedbackResponseMessageService,
    private val repository: FeedbackResponseMessageRepository,
) {

    @Scheduled(fixedDelay = 5000)
    fun sendMails() {
        repository.findByMessageSentAtIsNull().forEach { msg ->
            service.sendMail(msg)
        }
    }

}