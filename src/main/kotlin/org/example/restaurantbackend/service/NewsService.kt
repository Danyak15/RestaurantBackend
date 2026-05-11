package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.request.CreateNewsRequest
import org.example.restaurantbackend.dto.response.NewsResponse
import org.example.restaurantbackend.entity.NewsEntity
import org.example.restaurantbackend.repository.NewsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NewsService(
    private val newsRepository: NewsRepository
) {
    @Transactional(readOnly = true)
    fun getNews(restaurantId: Int?): List<NewsResponse> {
        val news = if (restaurantId == null) {
            newsRepository.findAll()
        } else {
            newsRepository.findByRestaurantId(restaurantId)
        }

        return news
            .sortedByDescending { it.createdAt }
            .map { it.toResponse() }
    }

    @Transactional
    fun createNews(request: CreateNewsRequest) {
        val news = NewsEntity().apply {
            restaurantId = request.restaurantId
            title = request.title
            createdAt = LocalDateTime.now().toString()
        }

        newsRepository.save(news)
    }

    @Transactional
    fun deleteNews(id: Long) {
        if (!newsRepository.existsById(id)) {
            throw IllegalArgumentException("Новость не найдена")
        }

        newsRepository.deleteById(id)
    }
}