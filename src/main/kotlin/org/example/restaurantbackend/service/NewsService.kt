package org.example.restaurantbackend.service

import org.example.restaurantbackend.dto.mappers.toResponse
import org.example.restaurantbackend.dto.news.CreateNewsRequest
import org.example.restaurantbackend.dto.news.UpdateNewsRequest
import org.example.restaurantbackend.dto.news.NewsResponse
import org.example.restaurantbackend.entity.NewsEntity
import org.example.restaurantbackend.repository.NewsRepository
import org.example.restaurantbackend.repository.RestaurantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class NewsService(
    private val newsRepository: NewsRepository,
    private val restaurantRepository: RestaurantRepository,
    private val fileStorageService: FileStorageService
) {
    @Transactional(readOnly = true)
    fun getNews(restaurantId: Long?): List<NewsResponse> {
        val news = if (restaurantId == null) {
            newsRepository.findAllByOrderByCreatedAtDesc()
        } else {
            newsRepository.findVisibleForRestaurant(restaurantId)
        }

        return news.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getNewsById(id: Long): NewsResponse {
        val news = newsRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Новость не найдена")

        return news.toResponse()
    }

    @Transactional
    fun createNews(request: CreateNewsRequest, imageFile: MultipartFile): NewsResponse {
        if (imageFile.isEmpty) {
            throw IllegalArgumentException("Фото новости обязательно")
        }

        val news = NewsEntity().apply {
            restaurant = request.restaurantId?.let { findRestaurant(it) }
            title = request.title
            content = request.content
            imageUrl = fileStorageService.saveNewsImage(imageFile)
        }

        return newsRepository.save(news).toResponse()
    }

    @Transactional
    fun updateNews(
        id: Long,
        request: UpdateNewsRequest,
        imageFile: MultipartFile? = null,
        replaceRestaurant: Boolean = false
    ): NewsResponse {
        val news = newsRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Новость не найдена") }

        val newImageFile = fileStorageService.replaceNewsImage(news.imageUrl, imageFile)

        if (replaceRestaurant) {
            news.restaurant = request.restaurantId?.let { findRestaurant(it) }
        } else {
            request.restaurantId?.let { news.restaurant = findRestaurant(it) }
        }

        request.title?.let { news.title = it }
        request.content?.let { news.content = it }
        newImageFile?.let { news.imageUrl = it }

        return newsRepository.save(news).toResponse()
    }

    @Transactional
    fun deleteNews(id: Long) {
        val news = newsRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Новость не найдена")

        fileStorageService.deleteNewsImage(news.imageUrl)
        newsRepository.delete(news)
    }

    private fun findRestaurant(restaurantId: Long) =
        restaurantRepository.findByIdOrNull(restaurantId)
            ?: throw IllegalArgumentException("Ресторан не найден")
}
