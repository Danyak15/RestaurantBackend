package org.example.restaurantbackend.dto.sync

import org.example.restaurantbackend.dto.category.CategoryResponse
import org.example.restaurantbackend.dto.dish.DishResponse
import org.example.restaurantbackend.dto.restaurant.RestaurantResponse

data class SyncResponse(
    val restaurants: List<RestaurantResponse>,
    val categories: List<CategoryResponse>,
    val dishes: List<DishResponse>
)
