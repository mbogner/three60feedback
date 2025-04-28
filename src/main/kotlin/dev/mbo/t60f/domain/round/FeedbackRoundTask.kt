package dev.mbo.t60f.domain.round

import dev.mbo.logging.logger
import dev.mbo.t60f.global.AsyncMailSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class FeedbackRoundTask(
    private val roundRepository: FeedbackRoundRepository,
    private val summaryService: FeedbackRoundSummaryService,
    private val mailer: AsyncMailSender,
) {

    private val log = logger()

    @Transactional
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    fun mail() {
        log.debug("mail ended rounds")
        roundRepository.findByValidityIsBeforeAndSummaryMailedIsFalse(Instant.now()).forEach { round: FeedbackRound ->
            log.debug("mail ended round: {}", round)
            val summary = summaryService.createSummary(round)

            val proxy = round.proxyReceiver
            val feedbackReceiver = round.receiver.email
            val mailReceiver = round.feedbackReceiver().email
            val byStr = if (null == proxy) "" else " requested by $mailReceiver"
            val summaryStr = round.summary ?: "No summary available."

            mailer.send(
                to = mailReceiver,
                subject = "Feedback round for $feedbackReceiver ended",
                content = """
Hi $mailReceiver!

feedback round for $feedbackReceiver$byStr has ended.
See below the summarised feedback:

$summaryStr

#Requested: ${summary.requested}
#Responded: ${summary.responded}

Yours,
t60f
""".trimIndent()
            )

            round.summaryMailed = true
            roundRepository.save(round)
        }
    }

}