package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.response.CategoryResponse
import org.example.restaurantbackend.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @GetMapping
    fun getCategories(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<List<CategoryResponse>> {
        val categories = categoryService.getCategoriesByRestaurantId(restaurantId)
        return ResponseEntity.ok(categories)
    }
}