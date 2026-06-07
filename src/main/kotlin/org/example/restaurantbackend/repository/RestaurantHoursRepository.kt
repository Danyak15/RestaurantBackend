package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.RestaurantHoursEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantHoursRepository : JpaRepository<RestaurantHoursEntity, Long> {
    fun findAllByRestaurantId(restaurantId: Long): List<RestaurantHoursEntity>
}