package org.example.restaurantbackend.service

import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoyaltyService(
    private val userRepository: UserRepository
) {
    fun addPoints(user: UserEntity, points: Int) {
        user.loyaltyPoints += points
        user.loyaltyLevel = calculateLevel(user.loyaltyPoints)
        userRepository.save(user)
    }

    fun calculateLevel(points: Int): String {
        return when {
            points >= 300 -> "GOLD"
            points >= 100 -> "SILVER"
            else -> "BRONZE"
        }
    }
}