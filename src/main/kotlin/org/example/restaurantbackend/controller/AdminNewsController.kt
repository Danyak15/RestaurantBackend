package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.request.CreateNewsRequest
import org.example.restaurantbackend.dto.request.UpdateNewsRequest
import org.example.restaurantbackend.dto.response.NewsResponse
import org.example.restaurantbackend.service.NewsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/news")
@PreAuthorize("hasRole('ADMIN')")
class AdminNewsController(
    private val newsService: NewsService
) {
    @PostMapping
    fun createNews(@RequestBody request: CreateNewsRequest): ResponseEntity<NewsResponse> {
        val news = newsService.createNews(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(news)
    }

    @PatchMapping("/{id}")
    fun updateNews(
        @PathVariable id: Long,
        @RequestBody request: UpdateNewsRequest
    ): ResponseEntity<NewsResponse> {
        return ResponseEntity.ok(newsService.updateNews(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteNews(@PathVariable id: Long): ResponseEntity<Unit> {
        newsService.deleteNews(id)
        return ResponseEntity.noContent().build()
    }
}