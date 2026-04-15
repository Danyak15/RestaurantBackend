package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.UserResponse
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository
) {
    @GetMapping("/me")
    fun me(authentication: Authentication): UserResponse {
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

}