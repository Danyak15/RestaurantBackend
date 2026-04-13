package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.LoginRequest
import org.example.restaurantbackend.dto.LoginResponse
import org.example.restaurantbackend.dto.RegisterRequest
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun registerUser(request: RegisterRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists")
        }

        val createdUser = UserEntity().apply {
            name = request.name
            surname = request.surname
            email = request.email
            passwordHash = passwordEncoder.encode(request.password).toString()
        }

        userRepository.save(createdUser)
    }

    fun loginUser(request: LoginRequest): LoginResponse? {
        val user = (userRepository.findByEmail(request.email))
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password")
        }

        return LoginResponse(
            id = user.id,
            name = user.name,
            surname = user.surname,
            email = user.email
        )
    }
}