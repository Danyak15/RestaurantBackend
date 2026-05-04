package org.example.restaurantbackend.dto.request

import java.time.LocalDateTime

data class ReservationRequest(
    val restaurantId: Int,
    val dateTime: LocalDateTime,
    val guests: Int
)
