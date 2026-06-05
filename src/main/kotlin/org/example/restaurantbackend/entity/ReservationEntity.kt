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
import org.example.restaurantbackend.entity.enums.ReservationStatus
import java.time.LocalDateTime

@Entity
@Table(name = "reservations")
class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: UserEntity

    @Column(name = "restaurant_id", nullable = false)
    var restaurantId: Int = 0

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    lateinit var table: RestaurantTableEntity

    @Column(name = "start_time", nullable = false)
    lateinit var startTime: LocalDateTime

    @Column(name = "end_time", nullable = false)
    lateinit var endTime: LocalDateTime

    @Column(nullable = false)
    var guests: Int = 0

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ReservationStatus = ReservationStatus.ACTIVE
}