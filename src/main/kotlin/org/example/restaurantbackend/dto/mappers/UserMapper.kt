package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.response.UserResponse
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.service.LoyaltyService
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val loyaltyService: LoyaltyService
) {
    fun toResponse(user: UserEntity) = UserResponse(
        id = user.id ?: throw IllegalStateException("Id пользователя null"),
        phone = user.phone,
        name = user.name,
        surname = user.surname,
        email = user.email,
        loyaltyPoints = user.loyaltyPoints,
        loyaltyLevel = loyaltyService.calculateLevel(user.loyaltyPoints)
    )
}