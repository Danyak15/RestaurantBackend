package org.example.restaurantbackend.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "restaurants")
class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false)
    var cuisine: String = ""

    @Column(nullable = false)
    var address: String = ""

    @Column(nullable = false)
    var description: String = ""

    @Column(nullable = false)
    var rating: Double = 0.0

    @Column(nullable = true)
    var phone: String? = null

    @OneToMany(mappedBy = "restaurant", cascade = [CascadeType.ALL], orphanRemoval = true)
    var workingHours: MutableList<RestaurantHoursEntity> = mutableListOf()

    @Column(name = "image_url", nullable = false)
    var imageUrl: String = ""
}