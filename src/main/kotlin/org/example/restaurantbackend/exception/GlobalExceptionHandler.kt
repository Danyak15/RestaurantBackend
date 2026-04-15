package org.example.restaurantbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult
            .allErrors
            .associate { error ->
                val fieldName = (error as FieldError).field
                fieldName to (error.defaultMessage ?: "Validation error")
            }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            mapOf(
                "status" to 400,
                "error" to "Validation failed",
                "fields" to errors
            )
        )
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<Map<String, Any?>> {
        return ResponseEntity.status(ex.statusCode).body(
            mapOf(
                "status" to ex.statusCode.value(),
                "error" to ex.reason
            )
        )
    }
}