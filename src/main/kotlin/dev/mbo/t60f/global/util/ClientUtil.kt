package dev.mbo.t60f.global.util

import dev.mbo.t60f.global.exc.ClientException
import org.springframework.http.ResponseEntity

object ClientUtil {

    fun assertStatus2xx(response: ResponseEntity<*>) {
        if (!response.statusCode.is2xxSuccessful) {
            throw ClientException(
                "received unexpected status code ${response.statusCode}",
                response.statusCode
            )
        }
    }

}