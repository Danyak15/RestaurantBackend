package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByPhone(phone: String): UserEntity?
    fun existsByPhone(phone: String): Boolean
}