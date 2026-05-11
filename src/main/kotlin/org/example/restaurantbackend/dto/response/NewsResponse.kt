package org.example.restaurantbackend.dto.response

data class NewsResponse(
    val id: Long,
    val restaurantId: Int?,
    val title: String,
    val content: String,
    val createdAt: String
)