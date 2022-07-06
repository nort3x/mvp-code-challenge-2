package com.github.nort3x.backendchallenge2.configuration

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class ExceptionHandlerConfig {
    @ExceptionHandler(IllegalArgumentException::class)
    fun onIllegalArg(ex: IllegalArgumentException, httpServletResponse: HttpServletResponse) {
        httpServletResponse.status = HttpStatus.BAD_REQUEST.value()
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.writer.write(
            """
            {
                "error" : "IllegalArgument",
                "message" : "${ex.message}"
            }
        """.trimIndent()
        )
        httpServletResponse.flushBuffer()

    }
}