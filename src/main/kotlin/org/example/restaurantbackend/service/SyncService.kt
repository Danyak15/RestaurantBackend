package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.sync.SyncResponse
import org.example.restaurantbackend.repository.CategoryRepository
import org.example.restaurantbackend.repository.DishRepository
import org.example.restaurantbackend.repository.RestaurantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SyncService(
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository,
    private val dishRepository: DishRepository
) {
    @Transactional(readOnly = true)
    fun getFullSync(): SyncResponse {
        val restaurants = restaurantRepository.findAll()
        val categories = categoryRepository.findAll()
        val dishes = dishRepository.findAll()

        return SyncResponse(
            restaurants = restaurants.map { it.toResponse() },
            categories = categories.map { it.toResponse() },
            dishes = dishes.map { it.toResponse() }
        )
    }
}
