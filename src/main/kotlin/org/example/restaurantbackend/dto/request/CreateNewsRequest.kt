package org.example.restaurantbackend.dto.request

data class CreateNewsRequest(
    val restaurantId: Int?,
    val title: String,
    val content: String,
)
