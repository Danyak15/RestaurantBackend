package org.example.restaurantbackend.dto.category

data class CategoryResponse(
    val id: Long,
    val restaurantId: Long,
    val name: String,
    val displayOrder: Int
)