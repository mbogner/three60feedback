package dev.mbo.t60f.global

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class EncryptorTest {

    private val encryptor = Encryptor(UUID.randomUUID().toString())

    @Test
    fun encrypt() {
        val randomString = UUID.randomUUID().toString()
        assertThat(randomString).isEqualTo(encryptor.decrypt(encryptor.encrypt(randomString)))
    }

}