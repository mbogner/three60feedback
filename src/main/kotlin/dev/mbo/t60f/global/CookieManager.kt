package dev.mbo.t60f.global

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.Instant
import java.util.*

@Suppress("unused")
object CookieManager {

    private const val COOKIE_VALIDITY = 60 * 60 * 24 * 365 * 4 + 1 // 4 years in seconds

    fun <T> retrieve(
        param: T?,
        request: HttpServletRequest,
        cookieName: String,
        parser: (String) -> T
    ): T? {
        return param ?: request.cookies
            ?.firstOrNull { it.name == cookieName }
            ?.value
            ?.let { runCatching { parser(it) }.getOrNull() }
    }

    fun <T> update(
        value: T?,
        response: HttpServletResponse,
        cookieName: String,
        serializer: (T) -> String,
        validity: Int = COOKIE_VALIDITY,
    ) {
        val cookieValue = value?.let { serializer(it) }
        val cookie = Cookie(cookieName, cookieValue)
        cookie.path = "/"              // Cookie is valid for the entire site
        cookie.isHttpOnly = true       // Cannot be accessed via JavaScript
        cookie.maxAge = if (value == null) 0 else validity
        response.addCookie(cookie)
    }

    // ======== Predefined Parsers ========
    val uuidParser: (String) -> UUID = UUID::fromString
    val stringParser: (String) -> String = { it }
    val intParser: (String) -> Int = String::toInt
    val longParser: (String) -> Long = String::toLong
    val instantParser: (String) -> Instant = Instant::parse

    // ======== Predefined Serializers ========
    val uuidSerializer: (UUID) -> String = UUID::toString
    val stringSerializer: (String) -> String = { it }
    val intSerializer: (Int) -> String = Int::toString
    val longSerializer: (Long) -> String = Long::toString
    val instantSerializer: (Instant) -> String = Instant::toString
}
