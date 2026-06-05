package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.UserMapper
import org.example.restaurantbackend.dto.request.LoginRequest
import org.example.restaurantbackend.dto.response.LoginResponse
import org.example.restaurantbackend.dto.request.RegisterRequest
import org.example.restaurantbackend.dto.request.UpdateUserRequest
import org.example.restaurantbackend.dto.response.UserResponse
import org.example.restaurantbackend.entity.UserEntity
import org.example.restaurantbackend.entity.enums.UserRole
import org.example.restaurantbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val userMapper: UserMapper
) {
    fun registerUser(request: RegisterRequest) {
        if (userRepository.existsByPhone(request.phone)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с таким номером уже существует")
        }

        val createdUser = UserEntity().apply {
            role = UserRole.USER
            name = request.name
            surname = request.surname
            phone = request.phone
            passwordHash = passwordEncoder.encode(request.password)
                ?: throw IllegalStateException("Пароль пользователя null")
        }

        userRepository.save(createdUser)
    }

    fun loginUser(request: LoginRequest): LoginResponse {
        val user = (userRepository.findByPhone(request.phone))
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный телефон или пароль")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный телефон или пароль")
        }

        val token = jwtService.generateToken(user)

        return LoginResponse(
            token = token,
            user = userMapper.toResponse(user)
        )
    }

    fun getUser(userId: Long): UserResponse {
        val user = findUser(userId)
        return userMapper.toResponse(user)
    }

    fun updateUser(userId: Long, request: UpdateUserRequest): UserResponse {
        val user = findUser(userId)

        val email = request.email
            ?.trim()
            ?.takeIf { it.isNotBlank() }

        user.name = request.name
        user.surname = request.surname
        user.email = email
        val changedUser = userRepository.save(user)

        return userMapper.toResponse(changedUser)
    }

    private fun findUser(userId: Long): UserEntity {
        return userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("Пользователь не найден") }
    }
}