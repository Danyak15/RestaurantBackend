package org.example.restaurantbackend.service

import org.example.restaurantbackend.entity.FavoriteEntity
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.repository.FavoriteRepository
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository
) {
    fun getFavorites(userId: Long): List<Int> {
        val user = findUser(userId)
        return favoriteRepository.findAllByUser(user).map { it.dishId }
    }

    fun addFavorite(userId: Long, dishId: Int) {
        val user = findUser(userId)

        if (!favoriteRepository.existsByUserAndDishId(user, dishId)) {
            val favorite = FavoriteEntity().apply {
                this.user = user
                this.dishId = dishId
            }

            favoriteRepository.save(favorite)
        }
    }

    fun removeFavorite(userId: Long, dishId: Int) {
        val user = findUser(userId)

        val favorite = favoriteRepository.findByUserAndDishId(user, dishId)

        if (favorite != null) {
            favoriteRepository.delete(favorite)
        }
    }

    private fun findUser(userId: Long): UserEntity {
        return userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("Пользователь не найден") }
    }
}