package org.example.restaurantbackend.controller

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.request.UpdateUserRequest
import org.example.restaurantbackend.dto.response.UserResponse
import org.example.restaurantbackend.repository.UserRepository
import org.example.restaurantbackend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMe(authentication: Authentication): UserResponse {
        val email = authentication.name

        val user = userRepository.findByEmail(email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        return UserResponse(
            id = user.id,
            name = user.name,
            surname = user.surname,
            email = user.email
        )
    }

    @PutMapping("/me")
    fun updateMe(
        authentication: Authentication,
        @Valid @RequestBody request: UpdateUserRequest
    ): UserResponse {
        return userService.updateUser(authentication.name, request)
    }

}