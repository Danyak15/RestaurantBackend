package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.response.UserResponse
import org.example.restaurantbackend.entity.UserEntity

fun UserEntity.toResponse() = UserResponse(
    id = id ?: throw IllegalStateException("Id пользователя null"),
    phone = phone,
    name = name,
    surname = surname,
    email = email
)