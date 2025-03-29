package dev.mbo.t60f

import io.restassured.RestAssured.given
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.Test

class SpringBootMailTest : AbstractSpringBootMailTest() {

    @Test
    fun checkNoMails() {
        given().spec(mailpitApiSpec).`when`().get("/messages")
            .then()
            .body("total", greaterThanOrEqualTo(0))
    }

}