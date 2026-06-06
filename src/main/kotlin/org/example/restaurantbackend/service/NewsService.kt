package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.request.CreateNewsRequest
import org.example.restaurantbackend.dto.request.UpdateNewsRequest
import org.example.restaurantbackend.dto.response.NewsResponse
import org.example.restaurantbackend.entity.NewsEntity
import org.example.restaurantbackend.repository.NewsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

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

    @Transactional(readOnly = true)
    fun getNewsById(id: Long): NewsResponse {
        val news = newsRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Новость не найдена")

        return news.toResponse()
    }

    @Transactional
    fun createNews(request: CreateNewsRequest): NewsResponse {
        val news = NewsEntity().apply {
            restaurantId = request.restaurantId
            title = request.title
            content = request.content
        }

        return newsRepository.save(news).toResponse()
    }

    @Transactional
    fun updateNews(id: Long, request: UpdateNewsRequest): NewsResponse {
        val news = newsRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Новость не найдена") }

        request.restaurantId?.let { news.restaurantId = it }
        request.title?.let { news.title = it }
        request.content?.let { news.content = it }

        return newsRepository.save(news).toResponse()
    }

    @Transactional
    fun deleteNews(id: Long) {
        if (!newsRepository.existsById(id)) {
            throw IllegalArgumentException("Новость не найдена")
        }

        newsRepository.deleteById(id)
    }
}