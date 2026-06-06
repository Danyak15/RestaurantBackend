package org.example.restaurantbackend.dto.response

import java.time.LocalDate

data class NewsResponse(
    val id: Long,
    val restaurantId: Int?,
    val title: String,
    val content: String,
    val createdAt: LocalDate
)