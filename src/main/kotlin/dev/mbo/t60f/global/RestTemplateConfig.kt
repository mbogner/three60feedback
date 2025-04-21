package dev.mbo.t60f.global

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.util.Timeout
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit

@Configuration
class RestTemplateConfig(
    @Value("\${app.client.timeout:10000}")
    timeoutMs: Long,
) {

    private val timeout = Timeout.of(timeoutMs, TimeUnit.MILLISECONDS)

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        val template = builder.build()
        template.requestFactory = getClientHttpRequestFactory()
        return template
    }

    private fun getClientHttpRequestFactory(): ClientHttpRequestFactory {

        return HttpComponentsClientHttpRequestFactory(
            HttpClientBuilder.create()
                .setConnectionManager(PoolingHttpClientConnectionManager().apply {
                    setDefaultConnectionConfig(
                        ConnectionConfig.custom()
                            .setConnectTimeout(timeout)
                            .setSocketTimeout(timeout)
                            .build()
                    )
                })
                .setDefaultRequestConfig(
                    RequestConfig.custom()
                        .setResponseTimeout(timeout)
                        .setConnectionRequestTimeout(timeout)
                        .build()
                )
                .build()
        )
    }

}
