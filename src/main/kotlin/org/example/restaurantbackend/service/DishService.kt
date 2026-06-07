package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.dish.CreateDishRequest
import org.example.restaurantbackend.dto.dish.UpdateDishRequest
import org.example.restaurantbackend.dto.dish.DishResponse
import org.example.restaurantbackend.entity.DishEntity
import org.example.restaurantbackend.repository.CategoryRepository
import org.example.restaurantbackend.repository.DishRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val categoryRepository: CategoryRepository,
    private val fileStorageService: FileStorageService
) {

    @Transactional(readOnly = true)
    fun getDishesByCategory(categoryId: Long): List<DishResponse> {
        return dishRepository
            .findAllByCategoryIdOrderByDisplayOrderAscNameAsc(categoryId)
            .map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getAllDishes(): List<DishResponse> {
        return dishRepository.findAll()
            .map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getDishesByRestaurant(restaurantId: Long): List<DishResponse> {
        return dishRepository
            .findAllByCategoryRestaurantIdOrderByDisplayOrderAscNameAsc(restaurantId)
            .map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getDishById(dishId: Long): DishResponse {
        val dish = dishRepository.findByIdOrNull(dishId)
            ?: throw IllegalArgumentException("Блюдо не найдено")

        return dish.toResponse()
    }

    @Transactional
    fun createDish(
        categoryId: Long,
        request: CreateDishRequest,
        imageFile: MultipartFile
    ): DishResponse {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw IllegalArgumentException("Категория не найдена")

        val dishName = request.name.trim()

        if (dishRepository.existsByCategoryIdAndNameIgnoreCase(categoryId, dishName)) {
            throw IllegalArgumentException("Блюдо с таким названием уже существует")
        }

        val imageUrl = fileStorageService.saveDishImage(imageFile)

        val dish = DishEntity().apply {
            this.category = category
            name = dishName
            price = request.price
            weight = request.weight
            description = request.description.trim()
            displayOrder = request.displayOrder
            this.imageUrl = imageUrl
        }

        return dishRepository.save(dish).toResponse()
    }

    @Transactional
    fun updateDish(
        categoryId: Long,
        dishId: Long,
        request: UpdateDishRequest,
        imageFile: MultipartFile?
    ): DishResponse {
        val dish = dishRepository.findByIdOrNull(dishId)
            ?: throw IllegalArgumentException("Блюдо не найдено")

        val dishCategoryId = dish.category.id
            ?: throw IllegalStateException("Id категории null")

        if (dishCategoryId != categoryId) {
            throw IllegalArgumentException("Блюдо не относится к выбранной категории")
        }

        request.name?.let { name ->
            val dishName = name.trim()

            if (
                dishRepository.existsByCategoryIdAndNameIgnoreCaseAndIdNot(
                    categoryId,
                    dishName,
                    dishId
                )
            ) {
                throw IllegalArgumentException("Блюдо с таким названием уже существует")
            }

            dish.name = dishName
        }

        request.price?.let { dish.price = it }
        request.weight?.let { dish.weight = it }
        request.description?.let { dish.description = it.trim() }
        request.displayOrder?.let { dish.displayOrder = it }

        val imageUrl = fileStorageService.replaceDishImage(
            oldImageUrl = dish.imageUrl,
            newFile = imageFile
        )
        imageUrl?.let { dish.imageUrl = it }

        return dishRepository.save(dish).toResponse()
    }
}
