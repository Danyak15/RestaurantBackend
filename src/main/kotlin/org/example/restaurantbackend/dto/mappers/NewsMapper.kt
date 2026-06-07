package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.response.NewsResponse
import org.example.restaurantbackend.entity.NewsEntity

fun NewsEntity.toResponse() = NewsResponse(
    id = id ?: throw IllegalStateException("Id новости null"),
    restaurantId = restaurant?.id,
    title = title,
    content = content,
    createdAt = createdAt ?: throw IllegalStateException("Время создания новости null")
)
