package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:Email(message = "Email is not valid")
    @field:NotBlank(message = "Email must not be empty")
    val email: String,

    @field:Size(min = 6, message = "Password must have at least 6 characters")
    @field:NotBlank(message = "Password must not be empty")
    val password: String
)