package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.RestaurantTableEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface RestaurantTableRepository: JpaRepository<RestaurantTableEntity, Long> {
    fun findAllByRestaurantIdAndCapacityGreaterThanEqualOrderByCapacityAsc(
        restaurantId: Int,
        capacity: Int
    ): List<RestaurantTableEntity>
}