package org.example.restaurantbackend.scheduler

import jakarta.transaction.Transactional
import org.example.restaurantbackend.entity.enums.ReservationStatus
import org.example.restaurantbackend.repository.ReservationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ReservationScheduler(
    private val reservationRepository: ReservationRepository
) {
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    fun completeExpiredReservations() {
        val now = LocalDateTime.now()
        val expired = reservationRepository.findExpiredActive(now)

        if (expired.isEmpty()) return

        expired.forEach { it.status = ReservationStatus.COMPLETED }
        reservationRepository.saveAll(expired)
    }
}