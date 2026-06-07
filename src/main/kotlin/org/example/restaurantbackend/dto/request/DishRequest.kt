package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class DishRequest(
    @field:NotBlank(message = "Название блюда обязательно")
    var name: String = "",

    @field:Min(value = 0, message = "Цена должна быть больше 0")
    var price: Int = 0,

    @field:Min(value = 0, message = "Вес должен быть не меньше 0")
    var weight: Int = 0,

    @field:NotBlank(message = "Описание обязательно")
    var description: String = "",

    @field:Min(value = 0, message = "Порядок отображения должен быть не меньше 0")
    var displayOrder: Int = 0
)
