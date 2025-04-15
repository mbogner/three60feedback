package dev.mbo.t60f.config

import dev.mbo.kotlinencryption.Encryptor
import dev.mbo.t60f.AbstractSpringBootMailTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class EncryptorConfigTest @Autowired constructor(
    private val encryptor: Encryptor,
) : AbstractSpringBootMailTest() {

    @Test
    fun encryptor() {
        assertThat(encryptor).isNotNull
        val str = UUID.randomUUID().toString()
        val encrypted = encryptor.encrypt(str)
        assertThat(encryptor.decrypt(encrypted)).isEqualTo(str)
    }

}