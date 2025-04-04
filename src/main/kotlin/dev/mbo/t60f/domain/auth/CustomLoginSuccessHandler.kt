package dev.mbo.t60f.domain.auth

import dev.mbo.logging.logger
import dev.mbo.springkotlinweb.CookieManager
import dev.mbo.t60f.domain.user.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomLoginSuccessHandler(
    private val userRepository: UserRepository,
) : SavedRequestAwareAuthenticationSuccessHandler() {

    private val log = logger()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as UserDetails
        val username = principal.username

        if (username != "admin") {
            val companyId = retrieveCompanyIdOfUsername(username)
            log.debug("companyId of {} is {}", username, companyId)
            CookieManager.update(companyId, response, "companyId", CookieManager.uuidSerializer)
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }

    private fun retrieveCompanyIdOfUsername(username: String): UUID? {
        val user = userRepository.findByEmail(username) ?: return null
        return user.company?.id
    }
}