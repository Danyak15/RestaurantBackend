package org.example.restaurantbackend.dto.response

data class LoginResponse(
    val id: Long?,
    val name: String,
    val surname: String,
    val email: String
)