package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.restaurant.RestaurantHoursRequest
import org.example.restaurantbackend.dto.restaurant.RestaurantHoursResponse
import org.example.restaurantbackend.entity.RestaurantHoursEntity
import java.time.DayOfWeek

fun defaultRestaurantHours(): MutableList<RestaurantHoursRequest> {
    return DayOfWeek.entries.map { day ->
        RestaurantHoursRequest(
            dayOfWeek = day,
            openTime = null,
            closeTime = null,
            isClosed = true
        )
    }.toMutableList()
}

fun dayNames(): Map<DayOfWeek, String> {
    return mapOf(
        DayOfWeek.MONDAY to "Понедельник",
        DayOfWeek.TUESDAY to "Вторник",
        DayOfWeek.WEDNESDAY to "Среда",
        DayOfWeek.THURSDAY to "Четверг",
        DayOfWeek.FRIDAY to "Пятница",
        DayOfWeek.SATURDAY to "Суббота",
        DayOfWeek.SUNDAY to "Воскресенье"
    )
}

fun RestaurantHoursEntity.toResponse() = RestaurantHoursResponse(
    dayOfWeek = dayOfWeek,
    openTime = openTime,
    closeTime = closeTime,
    isClosed = isClosed,
)