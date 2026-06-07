package org.example.restaurantbackend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate

@Entity
@Table(name = "news")
class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = true)
    var restaurant: RestaurantEntity? = null

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var content: String = ""

    @Column(name = "image_url", nullable = false)
    var imageUrl: String = ""

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDate? = null
}
