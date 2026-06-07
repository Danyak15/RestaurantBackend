package org.example.restaurantbackend.controller

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.request.CategoryRequest
import org.example.restaurantbackend.service.CategoryService
import org.example.restaurantbackend.service.RestaurantService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/restaurants/{restaurantId}/categories")
class AdminCategoryController(
    private val categoryService: CategoryService,
    private val restaurantService: RestaurantService
) {
    @GetMapping
    fun listCategories(
        @PathVariable restaurantId: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)

        model.addAttribute("pageTitle", "Категории меню")
        model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / Категории")
        model.addAttribute("restaurant", restaurant)
        model.addAttribute("categories", categoryService.getCategoriesByRestaurantId(restaurantId))

        return "admin/categories/list"
    }

    @GetMapping("/create")
    fun createPage(
        @PathVariable restaurantId: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)

        model.addAttribute("pageTitle", "Добавить категорию")
        model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / Добавить категорию")
        model.addAttribute("restaurant", restaurant)
        model.addAttribute("formAction", "/admin/restaurants/$restaurantId/categories/create")
        model.addAttribute("request", CategoryRequest("", 0))

        return "admin/categories/form"
    }

    @PostMapping("/create")
    fun createCategory(
        @PathVariable restaurantId: Long,
        @Valid @ModelAttribute request: CategoryRequest,
        bindingResult: BindingResult,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Добавить категорию")
            model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / Добавить категорию")
            model.addAttribute("restaurant", restaurant)
            model.addAttribute("request", request)
            model.addAttribute("formAction", "/admin/restaurants/$restaurantId/categories/create")

            return "admin/categories/form"
        }

        return try {
            categoryService.createCategory(restaurantId, request)
            "redirect:/admin/restaurants/$restaurantId/categories"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("pageTitle", "Добавить категорию")
            model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / Добавить категорию")
            model.addAttribute("restaurant", restaurant)
            model.addAttribute("request", request)
            model.addAttribute("formAction", "/admin/restaurants/$restaurantId/categories/create")
            model.addAttribute("formError", e.message)

            "admin/categories/form"
        }
    }

    @GetMapping("/{categoryId}/edit")
    fun editPage(
        @PathVariable restaurantId: Long,
        @PathVariable categoryId: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)
        val category = categoryService.getCategoryById(categoryId)

        model.addAttribute("pageTitle", "Редактировать категорию")
        model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / Редактировать категорию")
        model.addAttribute("restaurant", restaurant)
        model.addAttribute("formAction", "/admin/restaurants/$restaurantId/categories/$categoryId/edit")
        model.addAttribute(
            "request",
            CategoryRequest(
                name = category.name,
                displayOrder = category.displayOrder
            )
        )

        return "admin/categories/form"
    }

    @PostMapping("/{categoryId}/edit")
    fun updateCategory(
        @PathVariable restaurantId: Long,
        @PathVariable categoryId: Long,
        @Valid @ModelAttribute("request") request: CategoryRequest,
        bindingResult: BindingResult,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Редактировать категорию")
            model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / Редактировать категорию")
            model.addAttribute("restaurant", restaurant)
            model.addAttribute("request", request)
            model.addAttribute("formAction", "/admin/restaurants/$restaurantId/categories/$categoryId/edit")

            return "admin/categories/form"
        }

        return try {
            categoryService.updateCategory(restaurantId, categoryId, request)
            "redirect:/admin/restaurants/$restaurantId/categories"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("pageTitle", "Редактировать категорию")
            model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / Редактировать категорию")
            model.addAttribute("restaurant", restaurant)
            model.addAttribute("request", request)
            model.addAttribute("formAction", "/admin/restaurants/$restaurantId/categories/$categoryId/edit")
            model.addAttribute("formError", e.message)

            "admin/categories/form"
        }
    }
}