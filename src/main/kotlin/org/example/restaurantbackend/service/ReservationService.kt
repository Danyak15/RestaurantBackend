package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.reservation.ReservationRequest
import org.example.restaurantbackend.dto.reservation.TimeSlotResponse
import org.example.restaurantbackend.entity.ReservationEntity
import org.example.restaurantbackend.entity.enums.ReservationStatus
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.repository.ReservationRepository
import org.example.restaurantbackend.repository.RestaurantRepository
import org.example.restaurantbackend.repository.RestaurantTableRepository
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class ReservationService(
    private val loyaltyService: LoyaltyService,
    private val restaurantRepository: RestaurantRepository,
    private val reservationRepository: ReservationRepository,
    private val restaurantTableRepository: RestaurantTableRepository,
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getReservations(userId: Long): List<ReservationEntity> {
        val user = findUser(userId)
        return reservationRepository.findAllByUserOrderByStartTimeAsc(user)
    }

    @Transactional
    fun createReservation(userId: Long, request: ReservationRequest): ReservationEntity {
        val user = findUser(userId)
        val restaurant = restaurantRepository.findById(request.restaurantId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Ресторан не найден") }

        val startTime = request.dateTime
        val endTime = startTime.plusHours(2)

        val suitableTables = restaurantTableRepository
            .findAllByRestaurantEntityIdAndCapacityGreaterThanEqualOrderByCapacityAsc(
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
        } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Нет свободных столиков на выбранное время")

        val reservation = ReservationEntity().apply {
            this.user = user
            this.restaurant = restaurant
            this.table = availableTable
            this.startTime = startTime
            this.endTime = endTime
            guests = request.guests
        }

        loyaltyService.addPoints(user, 10)

        return reservationRepository.save(reservation)
    }

    @Transactional
    fun cancelReservation(userId: Long, id: Long): ReservationEntity {
        val user = findUser(userId)

        val reservation = reservationRepository.findByIdAndUser(id, user)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Бронирование не найдено")

        if (reservation.status == ReservationStatus.CANCELLED) {
            return reservation
        }

        reservation.status = ReservationStatus.CANCELLED
        return reservationRepository.save(reservation)
    }

    @Transactional
    fun getAvailableTimes(
        restaurantId: Long,
        date: LocalDate,
        guests: Int
    ): List<TimeSlotResponse> {
        if (guests <= 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Количество гостей должно быть больше 0")
        }

        val result = mutableListOf<TimeSlotResponse>()

        var time = LocalTime.of(10, 0)
        val lastTime = LocalTime.of(23, 0)
        val now = LocalDateTime.now()

        val suitableTables = restaurantTableRepository
            .findAllByRestaurantEntityIdAndCapacityGreaterThanEqualOrderByCapacityAsc(
                restaurantId = restaurantId,
                capacity = guests
            )

        if (suitableTables.isEmpty()) return result

        val dayStart = LocalDateTime.of(date, LocalTime.of(10, 0))
        val dayEnd = LocalDateTime.of(date, LocalTime.of(23, 0)).plusHours(2)

        val activeReservations = reservationRepository.findActiveReservationsForPeriod(
            restaurantId = restaurantId,
            dayStart = dayStart,
            dayEnd = dayEnd
        )

        while (!time.isAfter(lastTime)) {
            val startTime = LocalDateTime.of(date, time)
            val endTime = startTime.plusHours(2)

            val busyTableIds = activeReservations
                .filter { reservation ->
                    reservation.startTime < endTime && reservation.endTime > startTime
                }
                .mapNotNull { reservation ->
                    reservation.table.id
                }
                .toSet()

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
    private fun findUser(userId: Long): UserEntity {
        return userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("Пользователь не найден") }
    }
}
