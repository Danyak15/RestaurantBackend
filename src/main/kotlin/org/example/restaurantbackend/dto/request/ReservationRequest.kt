package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import java.time.LocalDateTime

data class ReservationRequest(
    @field:Min(value = 1, message = "Id ресторана должен быть больше 0")
    val restaurantId: Long,

    @field:Future(message = "Дата бронирования должна быть в будущем")
    val dateTime: LocalDateTime,

    @field:Min(value = 1, message = "Количество гостей должно быть больше 0")
    val guests: Int
)
