package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CategoryRequest(
    @field:NotBlank(message = "Название категории обязательно")
    val name: String,

    @field:Min(value = 0, message = "Порядок не должен быть меньше 0")
    val displayOrder: Int
)
