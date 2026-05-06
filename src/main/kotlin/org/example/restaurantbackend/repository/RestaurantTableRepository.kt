package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.RestaurantTableEntity
import org.springframework.data.jpa.repository.JpaRepository


interface RestaurantTableRepository: JpaRepository<RestaurantTableEntity, Long> {
    fun findAllByRestaurantIdAndCapacityGreaterThanEqualOrderByCapacityAsc(
        restaurantId: Int,
        capacity: Int
    ): List<RestaurantTableEntity>
}