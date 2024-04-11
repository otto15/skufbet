package com.skufbet.userprofile.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleException(exception: ResponseStatusException): ResponseEntity<ApiHttpErrorDto?>? {
        return ResponseEntity
            .status(exception.statusCode)
            .body<ApiHttpErrorDto?>(ApiHttpErrorDto(exception.reason))
    }

    data class ApiHttpErrorDto(val message: String?)
}
