package org.example.restaurantbackend.service

import org.example.restaurantbackend.repository.AdminRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AdminDetailsService(
    private val adminRepository: AdminRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val admin = adminRepository.findByLogin(username)
            ?: throw UsernameNotFoundException("Админ не найден")

        return User.builder()
            .username(admin.login)
            .password(admin.password)
            .roles("ADMIN")
            .build()
    }
}