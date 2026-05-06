package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank(message = "Имя не должно быть пустым")
    val name: String,

    @field:NotBlank(message = "Фамилия не должна быть пустой")
    val surname: String,

    @field:Email(message = "Некорректный Email")
    val email: String?
)