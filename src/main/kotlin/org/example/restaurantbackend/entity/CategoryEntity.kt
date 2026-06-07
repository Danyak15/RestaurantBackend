package org.example.restaurantbackend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "categories")
class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    lateinit var restaurant: RestaurantEntity

    @Column(nullable = false)
    var name: String = ""

    @Column(name = "display_order", nullable = false)
    var displayOrder: Int = 1
}
