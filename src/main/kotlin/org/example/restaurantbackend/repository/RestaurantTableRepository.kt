package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.RestaurantTableEntity
import org.springframework.data.jpa.repository.JpaRepository


interface RestaurantTableRepository: JpaRepository<RestaurantTableEntity, Long> {
    fun findAllByRestaurantEntityIdAndCapacityGreaterThanEqualOrderByCapacityAsc(
        restaurantId: Long,
        capacity: Int
    ): List<RestaurantTableEntity>
}
