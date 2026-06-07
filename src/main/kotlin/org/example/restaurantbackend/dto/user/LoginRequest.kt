package org.example.restaurantbackend.dto.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "Номер телефона не должен быть пустым")
    @field:Pattern(
        regexp = """^\+7\d{10}$""",
        message = "Телефон должен быть РФ формата"
    )
    val phone: String,

    @field:NotBlank(message = "Пароль не должен быть пустым")
    @field:Size(min = 6, message = "Пароль должен состоять минимум из 6 символов")
    val password: String
)