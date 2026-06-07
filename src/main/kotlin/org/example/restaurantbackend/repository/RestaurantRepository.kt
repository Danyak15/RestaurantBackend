package org.example.restaurantbackend.repository

import org.example.restaurantbackend.entity.RestaurantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : JpaRepository<RestaurantEntity, Long>