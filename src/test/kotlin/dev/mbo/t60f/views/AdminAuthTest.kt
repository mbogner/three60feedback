package dev.mbo.t60f.views

import dev.mbo.t60f.AbstractSpringBootMailTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AdminAuthTest : AbstractSpringBootMailTest() {

    @Nested
    inner class AnonymousAccess {

        @Test
        fun indexAuth() {
            mockMvc().perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrlPattern("**/login"))
        }

        @Test
        fun companiesAuth() {
            mockMvc().perform(get("/admin/companies"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrlPattern("**/login"))
        }

        @Test
        fun roundsAuth() {
            mockMvc().perform(get("/admin/rounds"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrlPattern("**/login"))
        }
    }

    @Nested
    inner class UserAccess {

        @Test
        @WithMockUser(roles = ["USER"])
        fun indexForbidden() {
            mockMvc().perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden)
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun companiesForbidden() {
            mockMvc().perform(get("/admin/companies"))
                .andDo(print())
                .andExpect(status().isForbidden)
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun roundsForbidden() {
            mockMvc().perform(get("/admin/rounds"))
                .andDo(print())
                .andExpect(status().isForbidden)
        }
    }

    @Nested
    inner class AdminAccess {

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun indexOk() {
            mockMvc().perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun companiesOk() {
            mockMvc().perform(get("/admin/companies"))
                .andDo(print())
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun roundsOk() {
            mockMvc().perform(get("/admin/rounds"))
                .andDo(print())
                .andExpect(status().isOk)
        }
    }
}