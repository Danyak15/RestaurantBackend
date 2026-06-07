package org.example.restaurantbackend.config

import org.example.restaurantbackend.entity.AdminEntity
import org.example.restaurantbackend.repository.AdminRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class DatabaseSeeder(
    private val adminRepository: AdminRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (adminRepository.count() == 0L) {
            adminRepository.save(AdminEntity().apply {
                login = "admin"
                passwordHash = BCryptPasswordEncoder().encode("admin").toString()
            })
        }
    }
}