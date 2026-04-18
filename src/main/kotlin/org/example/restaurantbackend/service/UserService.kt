package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.request.LoginRequest
import org.example.restaurantbackend.dto.response.LoginResponse
import org.example.restaurantbackend.dto.request.RegisterRequest
import org.example.restaurantbackend.dto.request.UpdateUserRequest
import org.example.restaurantbackend.dto.response.UserResponse
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
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

    fun loginUser(request: LoginRequest): LoginResponse {
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

    fun updateUser(currentEmail: String, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findByEmail(currentEmail)
            ?: throw UsernameNotFoundException("User not found")

        if (user.email != request.email && userRepository.existsByEmail(request.email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists")
        }

        user.name = request.name
        user.surname = request.surname
        user.email = request.email

        val changedUser = userRepository.save(user)

        return UserResponse(
            id = changedUser.id,
            name = changedUser.name,
            surname = changedUser.surname,
            email = changedUser.email
        )
    }
}