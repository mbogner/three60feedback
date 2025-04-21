package dev.mbo.t60f.global.util

import dev.mbo.t60f.global.exc.ClientException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class ClientUtilTest {

    @Test
    fun assertStatus2xx() {
        ClientUtil.assertStatus2xx(ResponseEntity.status(200).build<String>())
        Assertions.assertThrows(ClientException::class.java) {
            ClientUtil.assertStatus2xx(ResponseEntity.status(400).build<String>())
        }
    }

}