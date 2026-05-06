package org.example.restaurantbackend.controller

import org.example.restaurantbackend.service.FavoriteService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/favorites")
class FavoriteController(
    private val favoriteService: FavoriteService
) {
    @GetMapping
    fun getFavorites(authentication: Authentication): List<Int> {
        val userId = authentication.name.toLong()
        return favoriteService.getFavorites(userId)
    }

    @PostMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addFavorite(
        authentication: Authentication,
        @PathVariable dishId: Int
    ) {
        val userId = authentication.name.toLong()
        favoriteService.addFavorite(userId, dishId)
    }

    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFavorite(
        authentication: Authentication,
        @PathVariable dishId: Int
    ) {
        val userId = authentication.name.toLong()
        favoriteService.removeFavorite(userId, dishId)
    }
}