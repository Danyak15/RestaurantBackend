package org.example.restaurantbackend.dto.response

data class RestaurantResponse(
    val id: Long,
    val name: String,
    val cuisine: String,
    val address: String,
    val description: String,
    val rating: Double,
    val phone: String?,
    val imageUrl: String,
    val workingHours: List<RestaurantHoursResponse>
)
