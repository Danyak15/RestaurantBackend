package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.request.ReservationRequest
import org.example.restaurantbackend.dto.response.TimeSlotResponse
import org.example.restaurantbackend.entity.ReservationEntity
import org.example.restaurantbackend.entity.ReservationStatus
import org.example.restaurantbackend.repository.ReservationRepository
import org.example.restaurantbackend.repository.RestaurantTableRepository
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val restaurantTableRepository: RestaurantTableRepository,
    private val userRepository: UserRepository
) {
    fun getReservations(email: String): List<ReservationEntity> {
        val user = findUser(email)
        return reservationRepository.findAllByUserOrderByStartTimeAsc(user)
    }

    fun createReservation(email: String, request: ReservationRequest): ReservationEntity {
        val user = findUser(email)

        if (request.guests <= 0) {
            throw IllegalArgumentException("Количество гостей должно быть больше 0")
        }

        if (request.dateTime.isBefore(LocalDateTime.now())) {
            throw IllegalArgumentException("Нельзя создать бронь на прошедшее время")
        }

        val startTime = request.dateTime
        val endTime = startTime.plusHours(2)

        val suitableTables = restaurantTableRepository
            .findAllByRestaurantIdAndCapacityGreaterThanEqualOrderByCapacityAsc(
                restaurantId = request.restaurantId,
                capacity = request.guests
            )

        val busyTables = reservationRepository.findBusyTableIds(
            restaurantId = request.restaurantId,
            startTime = startTime,
            endTime = endTime
        )

        val availableTable = suitableTables.firstOrNull { table ->
            val tableId = table.id
            tableId != null && tableId !in busyTables
        } ?: throw IllegalArgumentException("Нет свободных столиков на выбранное время")

        val reservation = ReservationEntity().apply {
            this.user = user
            this.restaurantId = request.restaurantId
            this.table = availableTable
            this.startTime = startTime
            this.endTime = endTime
            guests = request.guests
        }

        return reservationRepository.save(reservation)
    }

    fun cancelReservation(email: String, id: Long): ReservationEntity {
        val user = findUser(email)

        val reservation = reservationRepository.findByIdAndUser(id, user)
            ?: throw IllegalArgumentException("Бронирование не найдено")

        if (reservation.status == ReservationStatus.CANCELLED) {
            return reservation
        }

        reservation.status = ReservationStatus.CANCELLED
        return reservationRepository.save(reservation)
    }

    fun getAvailableTimes(
        restaurantId: Int,
        date: LocalDate,
        guests: Int
        ): List<TimeSlotResponse> {
        if (guests <= 0) {
            throw IllegalArgumentException("Количество гостей должно быть больше 0")
        }

        val result = mutableListOf<TimeSlotResponse>()

        var time = LocalTime.of(10, 0)
        val lastTime = LocalTime.of(23, 0)
        val now = LocalDateTime.now()

        val suitableTables = restaurantTableRepository
            .findAllByRestaurantIdAndCapacityGreaterThanEqualOrderByCapacityAsc(
                restaurantId = restaurantId,
                capacity = guests
            )

        if (suitableTables.isEmpty()) return result

        while (!time.isAfter(lastTime)) {
            val startTime = LocalDateTime.of(date, time)
            val endTime = startTime.plusHours(2)

            val busyTableIds = reservationRepository.findBusyTableIds(
                restaurantId = restaurantId,
                startTime = startTime,
                endTime = endTime
            )

            val hasAvailableTables = suitableTables.any { table ->
                val tableId = table.id
                tableId != null && tableId !in busyTableIds
            }

            if (startTime.isAfter(now) && hasAvailableTables) {
                result.add(TimeSlotResponse(time.toString()))
            }

            time = time.plusMinutes(30)
        }

        return result
    }

    private fun findUser(email: String) =
        userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("Пользователь не найден")
}