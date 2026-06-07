package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.dish.DishResponse
import org.example.restaurantbackend.entity.DishEntity

fun DishEntity.toResponse() = DishResponse(
    id = id ?: throw IllegalStateException("Id блюда null"),
    restaurantId = category.restaurant.id ?: throw IllegalStateException("Id ресторана null"),
    categoryId = category.id ?: throw IllegalStateException("Id категории null"),
    name = name,
    price = price,
    weight = weight,
    description = description,
    displayOrder = displayOrder,
    imageUrl = imageUrl
)