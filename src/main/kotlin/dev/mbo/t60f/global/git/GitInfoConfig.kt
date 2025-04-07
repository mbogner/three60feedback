package dev.mbo.t60f.global.git

import dev.mbo.logging.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.util.Properties

@Configuration
class GitInfoConfig {

    companion object {
        private const val FILE = "git.properties"
    }

    private val log = logger()

    @Bean
    fun gitInfo(resourceLoader: ResourceLoader): GitInfo {
        val props = Properties()
        return try {
            val resource = resourceLoader.getResource("classpath:$FILE")
            resource.inputStream.use {
                props.load(it)
            }
            val commitFull = props.getProperty("commitFull") ?: "Unknown"
            GitInfo(commitFull)
        } catch (e: Exception) {
            log.error("could not load $FILE from classpath", e)
            GitInfo(commitFull = "Unknown")
        }
    }
}