package dev.mbo.t60f

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@Import(TestcontainersConfiguration::class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = ["test"])
abstract class AbstractSpringBootTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @Test
    fun mockMvcInjection() {
        assertThat(mockMvc).isNotNull
        assertThat(mockMvc()).isEqualTo(mockMvc)
    }

    protected fun mockMvc(): MockMvc {
        return mockMvc!!
    }

}