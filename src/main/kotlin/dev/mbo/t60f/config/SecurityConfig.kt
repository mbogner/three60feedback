package dev.mbo.t60f.config

import dev.mbo.logging.logger
import dev.mbo.springkotlinweb.EnableModuleWeb
import dev.mbo.springkotlinweb.webmvc.CorsConfigModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableModuleWeb
@EnableWebSecurity
@Configuration
class SecurityConfig(
    @Value("\${app.auth.admin.username:admin}") private val adminUser: String,
    @Value("\${app.auth.admin.password:admin}") private val adminPass: String,
    private val corsConfigModel: CorsConfigModel,
) : WebMvcConfigurer {

    private val log = logger()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .sessionManagement { it.disable() }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(*arrayOf("/admin/**")).hasRole("ADMIN")
                    .anyRequest().permitAll()
            }
            .httpBasic(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
        val user = User.withUsername(adminUser)
            .password(passwordEncoder.encode(adminPass))
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(user)
    }

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

}