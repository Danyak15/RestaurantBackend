package org.example.restaurantbackend.dto.news

import jakarta.validation.constraints.Size

data class UpdateNewsRequest(
    val restaurantId: Long? = null,

    @field:Size(min = 1, message = "Заголовок не должен быть пустым")
    val title: String? = null,

    @field:Size(min = 1, message = "Контент не должен быть пустым")
    val content: String? = null
)