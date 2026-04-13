package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.RegisterRequest
import org.example.restaurantbackend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        userService.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully")
    }

}