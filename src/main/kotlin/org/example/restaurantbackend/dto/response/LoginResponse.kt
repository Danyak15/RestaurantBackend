package org.example.restaurantbackend.dto.response

data class LoginResponse(
    val token: String,
    val user: UserResponse
)