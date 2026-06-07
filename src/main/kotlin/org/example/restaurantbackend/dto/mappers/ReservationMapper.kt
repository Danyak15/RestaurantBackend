package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.reservation.ReservationResponse
import org.example.restaurantbackend.entity.ReservationEntity

fun ReservationEntity.toResponse() = ReservationResponse(
    id = this.id ?: throw IllegalStateException("Id брони null"),
    tableId = table.id ?: throw IllegalStateException("Id стола null"),
    restaurantId = this.restaurant.id ?: throw IllegalStateException("Id ресторана null"),
    startTime = this.startTime,
    endTime = this.endTime,
    guests = this.guests,
    status = this.status.toString()
)