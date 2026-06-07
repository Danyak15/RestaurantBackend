package org.example.restaurantbackend.dto.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.DayOfWeek
import java.time.LocalTime

data class RestaurantHoursRequest(
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,

    @field:DateTimeFormat(pattern = "HH:mm")
    val openTime: LocalTime? = null,

    @field:DateTimeFormat(pattern = "HH:mm")
    val closeTime: LocalTime? = null,

    val isClosed: Boolean = true
)