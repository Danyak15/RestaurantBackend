package org.example.restaurantbackend.dto.user

data class LoginResponse(
    val token: String,
    val user: UserResponse
)