package org.example.restaurantbackend.dto.mappers

import org.example.restaurantbackend.dto.news.NewsResponse
import org.example.restaurantbackend.entity.NewsEntity
import java.time.LocalDate

fun NewsEntity.toResponse() = NewsResponse(
    id = id ?: throw IllegalStateException("Id новости null"),
    restaurantId = restaurant?.id,
    title = title,
    content = content,
    imageUrl = imageUrl,
    createdAt = createdAt ?: LocalDate.now()
)
