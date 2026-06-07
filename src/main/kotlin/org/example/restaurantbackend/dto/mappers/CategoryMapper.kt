package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.response.CategoryResponse
import org.example.restaurantbackend.entity.CategoryEntity

fun CategoryEntity.toResponse() = CategoryResponse(
    id = id ?: throw IllegalStateException("Id категории null"),
    restaurantId = restaurant.id ?: throw IllegalStateException("Id ресторана null"),
    name = name,
    displayOrder = displayOrder
)