package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.response.NewsResponse
import org.example.restaurantbackend.service.NewsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/news")
class NewsController(
    private val newsService: NewsService
) {
    @GetMapping
    fun getNews(
        @RequestParam(required = false) restaurantId: Long?
    ): ResponseEntity<List<NewsResponse>> {
        val news = newsService.getNews(restaurantId)
        return ResponseEntity.ok(news)
    }
}
