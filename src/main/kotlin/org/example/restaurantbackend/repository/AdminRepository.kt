package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.AdminEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<AdminEntity, Long> {
    fun findByLogin(login: String): AdminEntity?
}