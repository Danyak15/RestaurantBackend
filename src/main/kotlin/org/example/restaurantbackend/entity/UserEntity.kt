package org.example.restaurantbackend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "users")
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false)
    var surname: String = ""

    @Column(nullable = false, unique = true)
    var email: String = ""

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String = ""
}