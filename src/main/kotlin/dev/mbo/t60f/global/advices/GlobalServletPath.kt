package dev.mbo.t60f.global.advices

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute


@ControllerAdvice
class GlobalServletPath {

    @ModelAttribute("servletPath")
    fun populateServletPath(request: HttpServletRequest): String = request.servletPath

}