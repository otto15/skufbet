package com.skufbet.balance.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleException(exception: ResponseStatusException): ResponseEntity<ApiHttpErrorTo?>? {
        return ResponseEntity.status(exception.statusCode)
            .body<ApiHttpErrorTo?>(ApiHttpErrorTo(exception.reason))
    }

    data class ApiHttpErrorTo(val message: String?)
}
