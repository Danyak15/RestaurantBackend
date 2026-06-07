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
@Table(name = "dishes")
class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    lateinit var category: CategoryEntity

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false)
    var price: Int = 0

    @Column(nullable = false)
    var description: String = ""

    @Column(nullable = false)
    var weight: Int = 0

    @Column(name = "image_url", nullable = false)
    var imageUrl: String = ""

    @Column(name = "display_order", nullable = false)
    var displayOrder: Int = 0
}