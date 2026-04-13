package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.RegisterRequest
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun registerUser(request: RegisterRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw RuntimeException("User with this email already exists")
        }

        val createdUser = UserEntity().apply {
            name = request.name
            surname = request.surname
            email = request.email
            passwordHash = request.password
        }

        userRepository.save(createdUser)
    }
}