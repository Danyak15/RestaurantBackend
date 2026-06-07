package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateDishRequest(
    @field:NotBlank(message = "Название блюда обязательно")
    var name: String? = null,

    @field:Min(value = 0, message = "Цена должна быть больше 0")
    var price: Int? = null,

    @field:Min(value = 0, message = "Вес должен быть не меньше 0")
    var weight: Int? = null,

    @field:NotBlank(message = "Описание обязательно")
    var description: String? = null,

    @field:Min(value = 1, message = "Порядок отображения должен быть не меньше 1")
    var displayOrder: Int? = null
)
