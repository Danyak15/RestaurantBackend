package org.example.restaurantbackend.controller

import org.example.restaurantbackend.dto.request.CreateNewsRequest
import org.example.restaurantbackend.dto.request.UpdateNewsRequest
import org.example.restaurantbackend.service.NewsService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/news")
class AdminNewsPageController(
    private val newsService: NewsService
) {
    @GetMapping
    fun listNews(model: Model): String {
        model.addAttribute("pageTitle", "Новости")
        model.addAttribute("breadcrumb", "Главная / Новости")
        model.addAttribute("newsList", newsService.getNews(null))

        return "admin/news/list"
    }

    @GetMapping("/create")
    fun createPage(model: Model): String {
        model.addAttribute("pageTitle", "Добавить новость")
        model.addAttribute("breadcrumb", "Главная / Новости / Добавить")
        model.addAttribute("formAction", "/admin/news/create")
        model.addAttribute("request", CreateNewsRequest(null, "", ""))

        return "admin/news/form"
    }

    @PostMapping("/create")
    fun createNews(@ModelAttribute request: CreateNewsRequest): String {
        newsService.createNews(
            request.copy(restaurantId = null)
        )

        return "redirect:/admin/news"
    }

    @GetMapping("/{id}/edit")
    fun editPage(@PathVariable id: Long, model: Model): String {
        val news = newsService.getNewsById(id)

        model.addAttribute("pageTitle", "Редактировать новость")
        model.addAttribute("breadcrumb", "Главная / Новости / Редактировать")
        model.addAttribute("formAction", "/admin/news/$id/edit")
        model.addAttribute(
            "request",
            UpdateNewsRequest(
                restaurantId = news.restaurantId,
                title = news.title,
                content = news.content,
            )
        )

        return "admin/news/form"
    }

    @PostMapping("/{id}/edit")
    fun updateNews(@PathVariable id: Long, @ModelAttribute request: UpdateNewsRequest): String {
        newsService.updateNews(
            id = id,
            request = request.copy(restaurantId = null)
        )

        return "redirect:/admin/news"
    }

    @PostMapping("/{id}/delete")
    fun deleteNews(
        @PathVariable id: Long
    ): String {
        newsService.deleteNews(id)

        return "redirect:/admin/news"
    }
}