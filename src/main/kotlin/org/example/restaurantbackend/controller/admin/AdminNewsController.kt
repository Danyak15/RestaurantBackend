package org.example.restaurantbackend.controller.admin

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.news.CreateNewsRequest
import org.example.restaurantbackend.dto.news.UpdateNewsRequest
import org.example.restaurantbackend.service.NewsService
import org.example.restaurantbackend.service.RestaurantService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/admin/news")
class AdminNewsController(
    private val newsService: NewsService,
    private val restaurantService: RestaurantService
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
        addNewsFormAttributes(
            model = model,
            request = CreateNewsRequest()
        )

        return "admin/news/form"
    }

    @PostMapping("/create")
    fun createNews(
        @Valid @ModelAttribute request: CreateNewsRequest,
        bindingResult: BindingResult,
        @RequestParam imageFile: MultipartFile,
        model: Model
    ): String {
        val imageError = if (imageFile.isEmpty) {
            "Фото новости обязательно"
        } else {
            null
        }

        if (bindingResult.hasErrors() || imageError != null) {
            addNewsFormAttributes(
                model = model,
                request = request,
                imageError = imageError
            )

            return "admin/news/form"
        }

        return try {
            newsService.createNews(request, imageFile)
            "redirect:/admin/news"
        } catch (e: IllegalArgumentException) {
            addNewsFormAttributes(
                model = model,
                request = request,
                formError = e.message
            )

            "admin/news/form"
        }
    }

    @GetMapping("/{id}/edit")
    fun editPage(@PathVariable id: Long, model: Model): String {
        val news = newsService.getNewsById(id)

        addNewsFormAttributes(
            model = model,
            request = UpdateNewsRequest(
                restaurantId = news.restaurantId,
                title = news.title,
                content = news.content,
            ),
            id = id,
            currentImageUrl = news.imageUrl
        )

        return "admin/news/form"
    }

    @PostMapping("/{id}/edit")
    fun updateNews(
        @PathVariable id: Long,
        @Valid @ModelAttribute("request") request: UpdateNewsRequest,
        bindingResult: BindingResult,
        @RequestParam("imageFile", required = false) imageFile: MultipartFile?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            val news = newsService.getNewsById(id)

            addNewsFormAttributes(
                model = model,
                request = request,
                id = id,
                currentImageUrl = news.imageUrl
            )

            return "admin/news/form"
        }

        return try {
            newsService.updateNews(
                id = id,
                request = request,
                imageFile = imageFile,
                replaceRestaurant = true
            )

            "redirect:/admin/news"
        } catch (e: IllegalArgumentException) {
            val news = newsService.getNewsById(id)

            addNewsFormAttributes(
                model = model,
                request = request,
                id = id,
                currentImageUrl = news.imageUrl,
                formError = e.message
            )

            "admin/news/form"
        }
    }

    @PostMapping("/{id}/delete")
    fun deleteNews(
        @PathVariable id: Long
    ): String {
        newsService.deleteNews(id)

        return "redirect:/admin/news"
    }

    private fun addNewsFormAttributes(
        model: Model,
        request: Any,
        id: Long? = null,
        currentImageUrl: String? = null,
        formError: String? = null,
        imageError: String? = null
    ) {
        val isEdit = id != null

        model.addAttribute("pageTitle", if (isEdit) "Редактировать новость" else "Добавить новость")
        model.addAttribute(
            "breadcrumb",
            if (isEdit) {
                "Главная / Новости / Редактировать"
            } else {
                "Главная / Новости / Добавить"
            }
        )
        model.addAttribute("formAction", if (id == null) "/admin/news/create" else "/admin/news/$id/edit")
        model.addAttribute("request", request)
        model.addAttribute("restaurants", restaurantService.getRestaurants())
        model.addAttribute("isEdit", isEdit)

        currentImageUrl?.let { model.addAttribute("currentImageUrl", it) }
        formError?.let { model.addAttribute("formError", it) }
        imageError?.let { model.addAttribute("imageError", it) }
    }
}