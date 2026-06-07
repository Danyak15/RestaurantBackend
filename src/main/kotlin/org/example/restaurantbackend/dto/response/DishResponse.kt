package org.example.restaurantbackend.dto.response

data class DishResponse(
    val id: Long,
    val restaurantId: Long,
    val categoryId: Long,
    val name: String,
    val price: Int,
    val weight: Int,
    val description: String,
    val displayOrder: Int,
    val imageUrl: String
)
