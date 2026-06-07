package org.example.restaurantbackend.dto.news

import jakarta.validation.constraints.NotBlank

data class CreateNewsRequest(
    val restaurantId: Long? = null,

    @field:NotBlank(message = "Заголовок не должен быть пустым")
    val title: String = "",

    @field:NotBlank(message = "Контент не должен быть пустым")
    val content: String = ""
)