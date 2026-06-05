package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.ReservationEntity
import org.example.restaurantbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ReservationRepository : JpaRepository<ReservationEntity, Long> {
    fun findAllByUserOrderByStartTimeAsc(user: UserEntity): List<ReservationEntity>
    fun findByIdAndUser(id: Long, user: UserEntity): ReservationEntity?

    @Query("""
       SELECT r.table.id FROM ReservationEntity r
       WHERE r.restaurantId = :restaurantId
       AND r.startTime < :endTime
       AND r.endTime > :startTime
       AND r.status = org.example.restaurantbackend.entity.enums.ReservationStatus.ACTIVE
    """)
    fun findBusyTableIds(
        restaurantId: Int,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<Long>
}