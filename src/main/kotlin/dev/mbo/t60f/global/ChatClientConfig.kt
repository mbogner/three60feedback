package dev.mbo.t60f.global

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfig(
    private val builder: ChatClient.Builder
) {

    @Bean
    fun chatClient(): ChatClient {
        return builder
            .build()
    }

}
