package dev.mbo.t60f.domain.response.message

import dev.mbo.t60f.domain.response.FeedbackResponse
import dev.mbo.t60f.domain.round.FeedbackRound
import dev.mbo.t60f.domain.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

class FeedbackResponseMessageServiceTest {

    companion object {
        const val ALICE = "alice@example.com"
        const val BOB = "bob@example.com"
        const val CHIARA = "chiara@example.com"
    }

    @Nested
    inner class ProxyCase {
        // round started for alice with bob as proxy and a response from chiara
        val response1 = FeedbackResponse(
            feedbackRound = FeedbackRound(
                receiver = User(
                    email = ALICE,
                ),
                proxyReceiver = User(
                    email = BOB,
                ),
                validity = Instant.now()
            ),
            email = CHIARA,
        )
        val response1Message1 = FeedbackResponseMessage(
            feedbackResponse = response1,
            senderMail = BOB,
            message = "are you serious?"
        )
        val response1Message2 = FeedbackResponseMessage(
            feedbackResponse = response1,
            senderMail = ALICE,
            message = "for sure!"
        )

        @BeforeEach
        fun before() {
            response1.messages = mutableListOf(response1Message1, response1Message2)
        }

        @Test
        fun bobToChiara() {
            // bob is logged in and asks something to chiara
            assertThat(FeedbackResponseMessageService.calculateTo(BOB, response1Message1)).isEqualTo(CHIARA)
        }

        @Test
        fun chiaraToBob() {
            // chiara answers to bob
            assertThat(FeedbackResponseMessageService.calculateTo(CHIARA, response1Message2)).isEqualTo(BOB)
        }
    }

    @Nested
    inner class SimpleCase {

        // round started by alice without proxy with a response from bob
        val response1 = FeedbackResponse(
            feedbackRound = FeedbackRound(
                receiver = User(
                    email = ALICE,
                ),
                proxyReceiver = null,
                validity = Instant.now()
            ),
            email = BOB,
        )
        val response1Message1 = FeedbackResponseMessage(
            feedbackResponse = response1,
            senderMail = ALICE,
            message = "are you serious?"
        )
        val response1Message2 = FeedbackResponseMessage(
            feedbackResponse = response1,
            senderMail = BOB,
            message = "for sure!"
        )

        @BeforeEach
        fun before() {
            response1.messages = mutableListOf(response1Message1, response1Message2)
        }

        @Test
        fun aliceToBob() {
            // alice is logged in and writes a message to bob
            assertThat(FeedbackResponseMessageService.calculateTo(ALICE, response1Message1)).isEqualTo(BOB)
        }

        @Test
        fun bobToAlice() {
            // bob is logged in and answers to alice
            assertThat(FeedbackResponseMessageService.calculateTo(BOB, response1Message2)).isEqualTo(ALICE)
        }
    }

}