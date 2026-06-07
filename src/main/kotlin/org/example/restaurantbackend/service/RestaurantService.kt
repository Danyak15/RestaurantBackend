package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.request.CreateRestaurantRequest
import org.example.restaurantbackend.dto.request.RestaurantHoursRequest
import org.example.restaurantbackend.dto.request.UpdateRestaurantRequest
import org.example.restaurantbackend.dto.response.RestaurantResponse
import org.example.restaurantbackend.entity.RestaurantEntity
import org.example.restaurantbackend.entity.RestaurantHoursEntity
import org.example.restaurantbackend.repository.RestaurantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class RestaurantService(
    private val restaurantRepository : RestaurantRepository,
    private val fileStorageService: FileStorageService
) {
    @Transactional(readOnly = true)
    fun getRestaurants(): List<RestaurantResponse> {
        return restaurantRepository.findAll()
            .map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getRestaurantById(id: Long): RestaurantResponse {
        val restaurant = restaurantRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Ресторан не найден")

        return restaurant.toResponse()
    }

    @Transactional
    fun createRestaurant(
        request: CreateRestaurantRequest,
        imageFile: MultipartFile
    ): RestaurantResponse {
        val imageUrl = fileStorageService.saveRestaurantImage(imageFile)

        val restaurant = RestaurantEntity().apply {
            name = request.name.trim()
            cuisine = request.cuisine.trim()
            address = request.address.trim()
            description = request.description.trim()
            rating = request.rating
            phone = request.phone?.trim()?.takeIf { it.isNotBlank() }
            this.imageUrl = imageUrl
        }

        restaurant.workingHours.addAll(
            buildWorkingHours(restaurant, request.workingHours)
        )

        return restaurantRepository.save(restaurant).toResponse()
    }

    @Transactional
    fun updateRestaurant(
        id: Long,
        request: UpdateRestaurantRequest,
        imageFile: MultipartFile?
    ): RestaurantResponse {
        val restaurant = restaurantRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Ресторан не найден") }

        val newImageFile = fileStorageService.replaceRestaurantImage(
            oldImageUrl = restaurant.imageUrl,
            newFile = imageFile
        )

        request.name?.let { restaurant.name = it.trim() }
        request.cuisine?.let { restaurant.cuisine = it.trim() }
        request.address?.let { restaurant.address = it.trim() }
        request.description?.let { restaurant.description = it.trim() }
        request.rating?.let { restaurant.rating = it }
        request.phone?.let { phone ->
            restaurant.phone = phone.trim().takeIf { it.isNotBlank() }
        }
        newImageFile?.let { restaurant.imageUrl = it }

        request.workingHours?.let { workingHours ->
            restaurant.workingHours.clear()
            restaurant.workingHours.addAll(
                buildWorkingHours(restaurant, workingHours)
            )
        }

        return restaurantRepository.save(restaurant).toResponse()
    }

    private fun buildWorkingHours(
        restaurant: RestaurantEntity,
        hoursRequests: List<RestaurantHoursRequest>
    ): MutableList<RestaurantHoursEntity> {
        if (hoursRequests.size != 7) {
            throw IllegalArgumentException("Нужно указать расписание на 7 дней")
        }

        return hoursRequests.map { request ->
            if (!request.isClosed) {
                if (request.openTime == null || request.closeTime == null) {
                    throw IllegalArgumentException("Для рабочего дня нужно указать время открытия и закрытия")
                }

                if (!request.closeTime.isAfter(request.openTime)) {
                    throw IllegalArgumentException("Время закрытия должно быть позже времени открытия")
                }
            }

            RestaurantHoursEntity().apply {
                this.restaurant = restaurant
                this.dayOfWeek = request.dayOfWeek
                this.isClosed = request.isClosed
                this.openTime = if (request.isClosed) null else request.openTime
                this.closeTime = if (request.isClosed) null else request.closeTime
            }
        }.toMutableList()
    }
}
