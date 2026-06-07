package org.example.restaurantbackend.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "favorite_dishes",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "dish_id"])
    ]
)
class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: UserEntity

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    lateinit var dish: DishEntity
}
