package org.example.restaurantbackend.service

import org.example.restaurantbackend.entity.FavoriteEntity
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.repository.DishRepository
import org.example.restaurantbackend.repository.FavoriteRepository
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val dishRepository: DishRepository,
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getFavorites(userId: Long): List<Long> {
        val user = findUser(userId)
        return favoriteRepository.findAllByUser(user).map {
            it.dish.id ?: throw IllegalStateException("Id блюда null")
        }
    }

    @Transactional
    fun addFavorite(userId: Long, dishId: Long) {
        val user = findUser(userId)
        val dish = dishRepository.findByIdOrNull(dishId)
            ?: throw IllegalArgumentException("Блюдо не найдено")

        if (!favoriteRepository.existsByUserAndDishId(user, dishId)) {
            val favorite = FavoriteEntity().apply {
                this.user = user
                this.dish = dish
            }

            favoriteRepository.save(favorite)
        }
    }

    @Transactional
    fun removeFavorite(userId: Long, dishId: Long) {
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
