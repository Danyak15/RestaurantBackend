package org.example.restaurantbackend.controller.api

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.user.LoginRequest
import org.example.restaurantbackend.dto.user.RegisterRequest
import org.example.restaurantbackend.dto.user.LoginResponse
import org.example.restaurantbackend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<String> {
        userService.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь зарегестрирован")
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = userService.loginUser(request)
        return ResponseEntity.ok(response)
    }
}