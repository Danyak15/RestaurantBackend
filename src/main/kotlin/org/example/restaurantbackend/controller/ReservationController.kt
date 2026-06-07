package org.example.restaurantbackend.controller

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.request.ReservationRequest
import org.example.restaurantbackend.dto.response.ReservationResponse
import org.example.restaurantbackend.dto.response.TimeSlotResponse
import org.example.restaurantbackend.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val reservationService: ReservationService
) {
    @GetMapping("/me")
    fun getMyReservations(authentication: Authentication): ResponseEntity<List<ReservationResponse>> {
        val userId = authentication.name.toLong()

        val reservations = reservationService
            .getReservations(userId)
            .map { it.toResponse() }

        return ResponseEntity.ok(reservations)
    }

    @PostMapping
    fun createReservation(
        authentication: Authentication,
        @Valid @RequestBody request: ReservationRequest
        ): ResponseEntity<ReservationResponse> {
        val userId = authentication.name.toLong()

        val reservation = reservationService
            .createReservation(userId, request)
            .toResponse()

        return ResponseEntity.status(HttpStatus.CREATED).body(reservation)
    }

    @DeleteMapping("/{id}")
    fun deleteReservations(
        authentication: Authentication,
        @PathVariable id: Long
    ): ResponseEntity<ReservationResponse> {
        val userId = authentication.name.toLong()

        val reservation = reservationService
            .cancelReservation(userId, id)
            .toResponse()

        return ResponseEntity.ok(reservation)
    }

    @GetMapping("/available-times")
    fun getAvailableTimes(
        @RequestParam restaurantId: Long,
        @RequestParam date: LocalDate,
        @RequestParam guests: Int
    ): ResponseEntity<List<TimeSlotResponse>> {
        val slots = reservationService.getAvailableTimes(
            restaurantId = restaurantId,
            date = date,
            guests = guests
        )

        return ResponseEntity.ok(slots)
    }
}
