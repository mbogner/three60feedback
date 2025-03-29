package dev.mbo.t60f.global

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.*

object CookieManagerUUID {

    const val COOKIE_NAME = "companyId"
    private const val COOKIE_VALIDITY = 60 * 60 * 24 * 365 * 4 + 1 // 4 years in seconds

    fun retrieve(param: UUID?, request: HttpServletRequest): UUID? {
        return param ?: request.cookies
            ?.firstOrNull { it.name == COOKIE_NAME }
            ?.value
            ?.let { UUID.fromString(it) }
    }

    fun update(id: UUID?, response: HttpServletResponse) {
        val cookie = Cookie(COOKIE_NAME, id?.toString())
        cookie.path = "/"              // Cookie is valid for the entire site
        cookie.isHttpOnly = true       // Cannot be accessed via JavaScript
        cookie.maxAge = if (null == id) 0 else COOKIE_VALIDITY
        response.addCookie(cookie)
    }

}