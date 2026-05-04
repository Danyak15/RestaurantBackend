package org.example.restaurantbackend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "restaurant_tables")
class RestaurantTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "restaurant_id", nullable = false)
    var restaurantId: Int = 0

    @Column(nullable = false)
    var capacity: Int = 0
}