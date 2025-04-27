package dev.mbo.t60f.global.auth

import dev.mbo.logging.logger
import dev.mbo.springkotlinweb.webmvc.CorsConfigModel
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.TimeUnit


@Configuration
class WebMvcConfig(
    private val corsConfigModel: CorsConfigModel,
) : WebMvcConfigurer {

    private val log = logger()

    override fun addCorsMappings(registry: CorsRegistry) {
        for (entry in corsConfigModel.allowed) {
            log.info("adding cors config: {}", entry)
            registry.addMapping(entry.mapping)
                .allowedOriginPatterns(*entry.origins)
                .allowedHeaders(*entry.headers)
                .allowedMethods(*entry.methods)
                .exposedHeaders(*entry.exposedHeaders)
                .allowCredentials(entry.allowCredentials)
                .maxAge(entry.maxAge)
        }
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/")
            .setCacheControl(
                CacheControl
                    .maxAge(1, TimeUnit.DAYS)
                    .cachePublic()
            )
    }

}