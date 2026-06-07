package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.NewsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface NewsRepository : JpaRepository<NewsEntity, Long> {
    fun findAllByOrderByCreatedAtDesc(): List<NewsEntity>

    @Query(
        """
        SELECT news
        FROM NewsEntity news
        WHERE news.restaurant IS null
           OR news.restaurant.id = :restaurantId
        """
    )
    fun findVisibleForRestaurant(
        restaurantId: Long
    ): List<NewsEntity>
}
