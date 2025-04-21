package dev.mbo.t60f.global

import dev.mbo.kotlinencryption.Encryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EncryptorConfig(
    @Value("\${app.encryption.key:changeMe123!}") private val encryptionKey: String
) {

    @Bean
    fun encryptor(): Encryptor {
        return Encryptor(encryptionKey)
    }

}