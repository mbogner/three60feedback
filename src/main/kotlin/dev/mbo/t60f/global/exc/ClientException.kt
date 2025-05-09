package dev.mbo.t60f.global.exc

import org.springframework.http.HttpStatusCode

class ClientException : RuntimeException {

    private val statusCode: HttpStatusCode?

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