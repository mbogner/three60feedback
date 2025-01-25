package dev.mbo.t60f

import io.restassured.RestAssured.given
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

class SpringBootMailTest : AbstractSpringBootMailTest() {

    @Test
    fun checkNoMails() {
        given().spec(spec).`when`().get("/messages")
            .then()
            .body("total", equalTo(0))
            .body("count", equalTo(0))
            .body("items", empty<Any>())
    }

}