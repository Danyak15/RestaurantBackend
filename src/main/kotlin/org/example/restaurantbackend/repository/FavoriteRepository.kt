package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.FavoriteEntity
import org.example.restaurantbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteRepository : JpaRepository<FavoriteEntity, Long> {
    fun existsByUserAndDishId(user: UserEntity, dishId: Int): Boolean
    fun findByUserAndDishId(user: UserEntity, dishId: Int): FavoriteEntity?
    fun findAllByUser(user: UserEntity): List<FavoriteEntity>
}