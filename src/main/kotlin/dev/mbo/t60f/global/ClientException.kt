package dev.mbo.t60f.global

import org.springframework.http.HttpStatusCode

class ClientException : RuntimeException {

    private val statusCode: HttpStatusCode?

    constructor(message: String) : this(
        message,
        null,
        null
    )

    constructor(
        message: String,
        statusCode: HttpStatusCode
    ) : this(
        message,
        statusCode,
        null
    )

    constructor(
        message: String,
        cause: Throwable
    ) : this(
        message,
        null,
        cause
    )

    private constructor(
        message: String,
        statusCode: HttpStatusCode? = null,
        cause: Throwable? = null
    ) : super(
        message,
        cause
    ) {
        this.statusCode = statusCode
    }
}