package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank(message = "Name must not be empty")
    val name: String,

    @field:NotBlank(message = "Surname must not be empty")
    val surname: String,

    @field:Email(message = "Email is not valid")
    @field:NotBlank(message = "Email must not be empty")
    val email: String
)