package dev.mbo.t60f.global.git

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Properties

@Configuration
class GitInfoConfig {

    @Bean
    fun gitInfo(): GitInfo {
        val props = Properties()

        val resource = this::class.java.classLoader.getResourceAsStream("git.properties")
            ?: return GitInfo(commitFull = "Unknown")

        resource.use {
            props.load(it)
        }

        val commitFull = props.getProperty("commitFull") ?: "Unknown"
        return GitInfo(commitFull = commitFull)
    }
}