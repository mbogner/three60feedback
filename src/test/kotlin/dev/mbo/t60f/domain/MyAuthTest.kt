package dev.mbo.t60f.domain

import dev.mbo.t60f.AbstractSpringBootTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MyAuthTest : AbstractSpringBootTest() {

    @Nested
    inner class AnonymousAccess {

        @Test
        fun indexAuth() {
            mockMvc().perform(get("/my"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrlPattern("**/login"))
        }

        @Test
        fun roundsAuth() {
            mockMvc().perform(get("/my/rounds"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrlPattern("**/login"))
        }

        @Test
        fun proxRoundsAuth() {
            mockMvc().perform(get("/my/rounds/proxy"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrlPattern("**/login"))
        }

        @Test
        fun myOpen() {
            mockMvc().perform(get("/my/open-feedback"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrlPattern("**/login"))
        }
    }

    @Nested
    inner class UserAccess {

        @Test
        @WithMockUser(roles = ["USER"])
        fun indexAsUser() {
            mockMvc().perform(get("/my"))
                .andDo(print())
                .andExpect(status().isFound)
                .andExpect(redirectedUrl("/my/rounds"))
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun roundsAsUser() {
            mockMvc().perform(get("/my/rounds"))
                .andDo(print())
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun proxyRoundsAsUser() {
            mockMvc().perform(get("/my/rounds/proxy"))
                .andDo(print())
                .andExpect(status().isOk)
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun myOpenAsUser() {
            mockMvc().perform(get("/my/open-feedback"))
                .andDo(print())
                .andExpect(status().isOk)
        }
    }

    @Nested
    inner class AdminAccess {
        // admin has no rounds and no email

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun indexAsAdmin() {
            mockMvc().perform(get("/my"))
                .andDo(print())
                .andExpect(status().isForbidden)
        }

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun roundsAsAdmin() {
            mockMvc().perform(get("/my/rounds"))
                .andDo(print())
                .andExpect(status().isForbidden)
        }

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun proxyRoundsAsAdmin() {
            mockMvc().perform(get("/my/rounds/proxy"))
                .andDo(print())
                .andExpect(status().isForbidden)
        }

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun myOpenAsAdmin() {
            mockMvc().perform(get("/my/open-feedback"))
                .andDo(print())
                .andExpect(status().isForbidden)
        }
    }
}