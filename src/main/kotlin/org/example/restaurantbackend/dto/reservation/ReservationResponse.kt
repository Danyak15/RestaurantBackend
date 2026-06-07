package org.example.restaurantbackend.dto.reservation

import java.time.LocalDateTime

data class ReservationResponse(
    val id: Long,
    val tableId: Long,
    val restaurantId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val guests: Int,
    val status: String,
)