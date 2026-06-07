package org.example.restaurantbackend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.DayOfWeek
import java.time.LocalTime

@Entity
@Table(name = "restaurant_hours")
class RestaurantHoursEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    lateinit var restaurant: RestaurantEntity

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    var dayOfWeek: DayOfWeek = DayOfWeek.MONDAY

    @Column(name = "open_time", nullable = true)
    var openTime: LocalTime? = null

    @Column(name = "close_time", nullable = true)
    var closeTime: LocalTime? = null

    @Column(name = "is_closed", nullable = false)
    var isClosed: Boolean = false
}