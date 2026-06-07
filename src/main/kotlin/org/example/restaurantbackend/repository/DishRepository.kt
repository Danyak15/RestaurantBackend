package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.DishEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DishRepository : JpaRepository<DishEntity, Long> {
    fun findAllByCategoryIdOrderByDisplayOrderAscNameAsc(
        categoryId: Long
    ): List<DishEntity>

    fun findAllByCategoryRestaurantIdOrderByDisplayOrderAscNameAsc(
        restaurantId: Long
    ): List<DishEntity>

    fun existsByCategoryIdAndNameIgnoreCase(
        categoryId: Long,
        name: String
    ): Boolean

    fun existsByCategoryIdAndNameIgnoreCaseAndIdNot(
        categoryId: Long,
        name: String,
        id: Long
    ): Boolean
}