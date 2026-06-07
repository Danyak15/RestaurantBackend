package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.FavoriteEntity
import org.example.restaurantbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteRepository : JpaRepository<FavoriteEntity, Long> {
    fun existsByUserAndDishId(user: UserEntity, dishId: Long): Boolean
    fun findByUserAndDishId(user: UserEntity, dishId: Long): FavoriteEntity?
    fun findAllByUser(user: UserEntity): List<FavoriteEntity>
}
