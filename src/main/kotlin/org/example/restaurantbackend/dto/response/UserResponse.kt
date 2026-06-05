package org.example.restaurantbackend.dto.response

data class UserResponse(
    val id: Long,
    val role: String,
    val phone: String,
    val name: String,
    val surname: String,
    val email: String?,
    val loyaltyPoints: Int,
    val loyaltyLevel: String
)