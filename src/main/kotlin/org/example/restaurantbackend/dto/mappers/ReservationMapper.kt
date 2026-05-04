package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.response.ReservationResponse
import org.example.restaurantbackend.entity.ReservationEntity

fun ReservationEntity.toResponse() = ReservationResponse(
    id = this.id ?: 0,
    restaurantId = this.restaurantId,
    startTime = this.startTime,
    endTime = this.endTime,
    guests = this.guests,
    status = this.status.toString()
)