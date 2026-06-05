package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Size

data class UpdateNewsRequest(
    val restaurantId: Int?,

    @field:Size(min = 1, message = "Заголовок не должен быть пустым")
    val title: String?,

    @field:Size(min = 1, message = "Контент не должен быть пустым")
    val content: String?
)