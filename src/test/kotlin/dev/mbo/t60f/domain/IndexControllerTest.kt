package dev.mbo.t60f.domain

import dev.mbo.t60f.AbstractSpringBootTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class IndexControllerTest : AbstractSpringBootTest() {

    @Test
    fun index() {
        mockMvc().perform(get("/"))
            .andDo(print())
            .andExpect(status().isFound)
            .andExpect(redirectedUrl("/requests"))
    }

}