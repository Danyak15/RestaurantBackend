package org.example.restaurantbackend.service

import org.example.restaurantbackend.entity.FavoriteEntity
import org.example.restaurantbackend.repository.FavoriteRepository
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository
) {
    fun getFavorites(email: String): List<Int> {
        val user = findUser(email)

        return favoriteRepository.findAllByUser(user).map { it.dishId }
    }

    fun addFavorite(email: String, dishId: Int) {
        val user = findUser(email)

        if (!favoriteRepository.existsByUserAndDishId(user, dishId)) {
            val favorite = FavoriteEntity().apply {
                this.user = user
                this.dishId = dishId
            }

            favoriteRepository.save(favorite)
        }
    }

    fun removeFavorite(email: String, dishId: Int) {
        val user = findUser(email)

        val favorite = favoriteRepository.findByUserAndDishId(user, dishId)

        if (favorite != null) {
            favoriteRepository.delete(favorite)
        }
    }

    private fun findUser(email: String) =
        userRepository.findByEmail(email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")
}