package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.request.CreateNewsRequest
import org.example.restaurantbackend.dto.response.NewsResponse
import org.example.restaurantbackend.service.NewsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/news")
class NewsController(
    private val newsService: NewsService
) {
    @GetMapping
    fun getNews(
        @RequestParam(required = false) restaurantId: Int?
    ): ResponseEntity<List<NewsResponse>> {
        val news = newsService.getNews(restaurantId)
        return ResponseEntity.ok(news)
    }

    @PostMapping
    fun createNews(
        @RequestBody request: CreateNewsRequest
    ): ResponseEntity<Unit> {
        newsService.createNews(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(Unit)
    }

    @DeleteMapping("/{id}")
    fun deleteNews(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        newsService.deleteNews(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Unit)
    }
}