package org.example.restaurantbackend.dto.news

import java.time.LocalDate

data class NewsResponse(
    val id: Long,
    val restaurantId: Long?,
    val title: String,
    val content: String,
    val imageUrl: String,
    val createdAt: LocalDate
)