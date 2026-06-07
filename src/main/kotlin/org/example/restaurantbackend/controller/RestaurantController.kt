package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.response.RestaurantResponse
import org.example.restaurantbackend.service.RestaurantService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/restaurants")
class RestaurantController(
    private val restaurantService: RestaurantService
) {
    @GetMapping
    fun getRestaurants(): ResponseEntity<List<RestaurantResponse>> {
        val restaurants = restaurantService.getRestaurants()
        return ResponseEntity.ok(restaurants)
    }
}