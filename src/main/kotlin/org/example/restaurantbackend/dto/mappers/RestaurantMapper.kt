package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.response.RestaurantResponse
import org.example.restaurantbackend.entity.RestaurantEntity

fun RestaurantEntity.toResponse() = RestaurantResponse(
    id = id ?: throw IllegalStateException("ID ресторана null"),
    name = name,
    cuisine = cuisine,
    address = address,
    description = description,
    rating = rating,
    phone = phone,
    imageUrl = imageUrl,
    workingHours = workingHours.map { it.toResponse() }
)