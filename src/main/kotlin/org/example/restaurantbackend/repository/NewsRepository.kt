package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.NewsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NewsRepository : JpaRepository<NewsEntity, Long> {
    fun findByRestaurantId(restaurantId: Long): List<NewsEntity>
}
