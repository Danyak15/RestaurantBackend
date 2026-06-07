package org.example.restaurantbackend.dto.restaurant

import java.time.DayOfWeek
import java.time.LocalTime

data class RestaurantHoursResponse(
    val dayOfWeek: DayOfWeek,
    val openTime: LocalTime?,
    val closeTime: LocalTime?,
    val isClosed: Boolean
)