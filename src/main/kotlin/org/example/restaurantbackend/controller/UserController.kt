package org.example.restaurantbackend.controller

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.request.UpdateUserRequest
import org.example.restaurantbackend.dto.response.UserResponse
import org.example.restaurantbackend.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMe(authentication: Authentication): UserResponse {
        val userId = authentication.name.toLong()
        return userService.getUser(userId)
    }

    @PutMapping("/me")
    fun updateMe(
        authentication: Authentication,
        @Valid @RequestBody request: UpdateUserRequest
    ): UserResponse {
        val userId = authentication.name.toLong()
        return userService.updateUser(userId, request)
    }
}