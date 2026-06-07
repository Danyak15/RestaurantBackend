package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Min

data class RestaurantTableRequest(
    @field:Min(value = 1, message = "Количество гостей должно быть не меньше 1")
    val capacity: Int = 2,

    @field:Min(value = 1, message = "Количество столиков должно быть не меньше 1")
    val quantity: Int = 1
)
