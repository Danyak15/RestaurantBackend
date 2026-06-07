package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.request.CategoryRequest
import org.example.restaurantbackend.dto.response.CategoryResponse
import org.example.restaurantbackend.entity.CategoryEntity
import org.example.restaurantbackend.repository.CategoryRepository
import org.example.restaurantbackend.repository.RestaurantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository
) {
    @Transactional(readOnly = true)
    fun getCategoriesByRestaurantId(restaurantId: Long): List<CategoryResponse> {
        return categoryRepository
            .findAllByRestaurantIdOrderByDisplayOrderAsc(restaurantId)
            .map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getCategoryById(categoryId: Long): CategoryResponse {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw IllegalArgumentException("Категория не найдена")

        return category.toResponse()
    }

    @Transactional
    fun createCategory(restaurantId: Long, request: CategoryRequest): CategoryResponse {
        val restaurant = restaurantRepository.findByIdOrNull(restaurantId)
            ?: throw IllegalArgumentException("Ресторан не найден")

        if (categoryRepository.existsByRestaurantIdAndNameIgnoreCase(restaurantId, request.name.trim())) {
            throw IllegalArgumentException("Категория с таким названием уже существует")
        }

        val category = CategoryEntity().apply {
            this.restaurant = restaurant
            name = request.name.trim()
            displayOrder = request.displayOrder
        }

        return categoryRepository.save(category).toResponse()
    }

    @Transactional
    fun updateCategory(
        restaurantId: Long,
        categoryId: Long,
        request: CategoryRequest
    ): CategoryResponse {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw IllegalArgumentException("Категория не найдена")

        val categoryRestaurantId = category.restaurant.id
            ?: throw IllegalStateException("Id ресторана null")

        if (categoryRestaurantId != restaurantId) {
            throw IllegalArgumentException("Категория не относится к выбранному ресторану")
        }

        val categoryName = request.name.trim()

        if (
            categoryRepository.existsByRestaurantIdAndNameIgnoreCaseAndIdNot(
                restaurantId = restaurantId,
                name = categoryName,
                id = categoryId
            )
        ) {
            throw IllegalArgumentException("Категория с таким названием уже существует")
        }

        category.name = request.name.trim()
        category.displayOrder = request.displayOrder

        return categoryRepository.save(category).toResponse()
    }
}