package org.example.restaurantbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RestaurantBackendApplication

fun main(args: Array<String>) {
    runApplication<RestaurantBackendApplication>(*args)
}
