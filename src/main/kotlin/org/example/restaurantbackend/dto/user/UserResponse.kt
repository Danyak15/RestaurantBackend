package org.example.restaurantbackend.dto.user

data class UserResponse(
    val id: Long,
    val phone: String,
    val name: String,
    val surname: String,
    val email: String?,
    val loyaltyPoints: Int,
    val loyaltyLevel: String
)