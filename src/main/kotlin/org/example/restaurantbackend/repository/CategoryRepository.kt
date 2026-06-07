package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
    fun findAllByRestaurantIdOrderByDisplayOrderAsc(restaurantId : Long): List<CategoryEntity>
    fun existsByRestaurantIdAndNameIgnoreCase(restaurantId: Long, name: String): Boolean
    fun existsByRestaurantIdAndNameIgnoreCaseAndIdNot(
        restaurantId: Long,
        name: String,
        id: Long
    ): Boolean
}