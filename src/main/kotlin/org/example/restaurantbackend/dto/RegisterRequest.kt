package org.example.restaurantbackend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Name must not be empty")
    val name: String,

    @field:NotBlank(message = "Surname must not be empty")
    val surname: String,

    @field:Email(message = "Email is not valid")
    @field:NotBlank(message = "Email must not be empty")
    val email: String,

    @field:Size(min = 6, message = "Password must have at least 6 characters")
    @field:NotBlank(message = "Password must not be empty")
    val password: String
)
