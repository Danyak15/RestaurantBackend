package org.example.restaurantbackend.dto.response

import java.time.LocalDateTime

data class ReservationResponse(
    val id: Long,
    val tableId: Long,
    val restaurantId: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val guests: Int,
    val status: String,
)