package org.example.restaurantbackend.dto

data class UserResponse(
    val id: Long?,
    val name: String,
    val surname: String,
    val email: String
)