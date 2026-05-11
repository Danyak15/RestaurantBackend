package org.example.restaurantbackend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "news")
class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "restaurant_id", nullable = true)
    var restaurantId: Int? = null

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var content: String = ""

    @Column(nullable = false)
    var createdAt: String = ""
}