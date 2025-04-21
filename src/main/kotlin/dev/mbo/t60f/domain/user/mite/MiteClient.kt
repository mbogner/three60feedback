package dev.mbo.t60f.domain.user.mite

import dev.mbo.logging.logger
import dev.mbo.t60f.domain.user.mite.dto.MiteUserWrapperDto
import dev.mbo.t60f.global.exc.ClientException
import dev.mbo.t60f.global.util.ClientUtil
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

fun Map<String, Any>.getParamString(): String = keys.joinToString("&") { "$it={$it}" }

@Service
class MiteClient(
    private val restTemplate: RestTemplate,
) {

    private val log = logger()

    fun retrieveActiveUsers(
        baseUrl: String,
        apiKey: String,
        page: Int = 1,
        limit: Int = Int.MAX_VALUE
    ): List<MiteUserWrapperDto> {
        try {
            val params = mapOf<String, Any>(
                "api_key" to apiKey,
                "page" to page,
                "limit" to limit,
            )
            val response = restTemplate.exchange(
                usersUrl(baseUrl, params),
                HttpMethod.GET,
                HttpEntity<String>(
                    null,
                    HttpHeaders()
                ),
                object : ParameterizedTypeReference<List<MiteUserWrapperDto>>() {},
                params
            )
            ClientUtil.assertStatus2xx(response)
            return response.body ?: emptyList()
        } catch (exc: RestClientException) {
            throw ClientException("could not retrieve mite users", exc)
        }
    }

    private fun usersUrl(
        baseUrl: String,
        params: Map<String, Any>
    ): String {
        val url = "$baseUrl/users.json?${params.getParamString()}"
        log.debug("mite users url: {}", url)
        return url
    }

}