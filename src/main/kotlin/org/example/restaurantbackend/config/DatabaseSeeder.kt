package org.example.restaurantbackend.config

import org.example.restaurantbackend.entity.RestaurantTableEntity
import org.example.restaurantbackend.repository.RestaurantTableRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseSeeder(
    private val restaurantTableRepository: RestaurantTableRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (restaurantTableRepository.count() > 0) {
            return
        }

        val tables = listOf(
            RestaurantTableEntity().apply {
                restaurantId = 1
                capacity = 2
            },
            RestaurantTableEntity().apply {
                restaurantId = 1
                capacity = 2
            },
            RestaurantTableEntity().apply {
                restaurantId = 1
                capacity = 3
            },
            RestaurantTableEntity().apply {
                restaurantId = 1
                capacity = 4
            },
            RestaurantTableEntity().apply {
                restaurantId = 1
                capacity = 4
            },
            RestaurantTableEntity().apply {
                restaurantId = 1
                capacity = 6
            },
            RestaurantTableEntity().apply {
                restaurantId = 1
                capacity = 8
            },

            RestaurantTableEntity().apply {
                restaurantId = 2
                capacity = 2
            },
            RestaurantTableEntity().apply {
                restaurantId = 2
                capacity = 3
            },
            RestaurantTableEntity().apply {
                restaurantId = 2
                capacity = 3
            },
            RestaurantTableEntity().apply {
                restaurantId = 2
                capacity = 4
            },
            RestaurantTableEntity().apply {
                restaurantId = 2
                capacity = 4
            },
            RestaurantTableEntity().apply {
                restaurantId = 2
                capacity = 6
            },

            RestaurantTableEntity().apply {
                restaurantId = 3
                capacity = 2
            },
            RestaurantTableEntity().apply {
                restaurantId = 3
                capacity = 4
            },
            RestaurantTableEntity().apply {
                restaurantId = 3
                capacity = 6
            },
            RestaurantTableEntity().apply {
                restaurantId = 3
                capacity = 8
            }
        )

        restaurantTableRepository.saveAll(tables)
    }
}