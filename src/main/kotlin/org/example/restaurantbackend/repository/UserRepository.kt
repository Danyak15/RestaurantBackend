package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): UserEntity?
}