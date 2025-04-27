package dev.mbo.t60f.global.auth

import dev.mbo.logging.logger
import dev.mbo.springkotlinweb.EnableModuleWeb
import dev.mbo.springkotlinweb.webmvc.CorsConfigModel
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.LengthRule
import org.passay.PasswordValidator
import org.passay.WhitespaceRule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableModuleWeb
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true, // enables Spring Security pre-/post-annotations => The @PreAuthorize annotation checks the
    // given expression before entering the method, whereas, the @PostAuthorize annotation verifies it after the
    // execution of the method and could alter the result.
    securedEnabled = false, // determines if the @Secured annotation should be enabled
    jsr250Enabled = false, // allows us to use the @RoleAllowed annotation
)
@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun passwordValidator(): PasswordValidator = PasswordValidator(
        LengthRule(8, 128),
        CharacterRule(EnglishCharacterData.UpperCase, 1),
        CharacterRule(EnglishCharacterData.LowerCase, 1),
        CharacterRule(EnglishCharacterData.Digit, 1),
        CharacterRule(EnglishCharacterData.SpecialAscii, 1),
        WhitespaceRule()
    )

    @Bean
    fun configure(http: HttpSecurity, customLoginSuccessHandler: CustomLoginSuccessHandler): SecurityFilterChain {
        http
            .cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/login").permitAll() // allow auth related
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // allow assets
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/my/**").hasRole("USER")
                    .anyRequest().permitAll()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")                     // your custom login page
                    .loginProcessingUrl("/login")            // form action
                    .defaultSuccessUrl("/", true)            // go here after login
                    .failureUrl("/login?error=true")
                    .successHandler(customLoginSuccessHandler)
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutRequestMatcher(AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessUrl("/login?logout=true")
                    .permitAll()
            }
        return http.build()
    }

}