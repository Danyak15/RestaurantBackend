package org.example.restaurantbackend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.restaurantbackend.entity.enums.UserRole


@Entity
@Table(name = "users")
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.USER

    @Column(nullable = false, unique = true)
    var phone: String = ""

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false)
    var surname: String = ""

    @Column(nullable = true)
    var email: String? = null

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String = ""

    @Column(name = "loyalty_points", nullable = false)
    var loyaltyPoints: Int = 0

    @Column(name = "loyalty_level", nullable = false)
    var loyaltyLevel: String = "BRONZE"
}