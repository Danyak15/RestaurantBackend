package org.example.restaurantbackend.dto.category

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateCategoryRequest(
    @field:NotBlank(message = "Название категории обязательно")
    val name: String = "",

    @field:Min(value = 1, message = "Порядок должен быть не меньше 1")
    val displayOrder: Int = 1
)