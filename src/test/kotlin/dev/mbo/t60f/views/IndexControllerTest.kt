package dev.mbo.t60f.views

import dev.mbo.t60f.AbstractSpringBootMailTest
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class IndexControllerTest : AbstractSpringBootMailTest() {

    private fun index() {
        mockMvc().perform(MockMvcRequestBuilders.get("/"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun indexUnauthenticated() {
        index()
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun indexAdmin() {
        index()
    }

    @WithMockUser(roles = ["COACH"])
    @Test
    fun indexCoach() {
        index()
    }

    @WithMockUser(roles = ["USER"])
    @Test
    fun indexUser() {
        index()
    }

}