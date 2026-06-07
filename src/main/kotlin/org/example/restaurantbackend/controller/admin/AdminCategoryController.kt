package org.example.restaurantbackend.controller.admin

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.category.CreateCategoryRequest
import org.example.restaurantbackend.dto.category.UpdateCategoryRequest
import org.example.restaurantbackend.dto.restaurant.RestaurantResponse
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

        addCategoryFormAttributes(
            model = model,
            restaurant = restaurant,
            restaurantId = restaurantId,
            request = CreateCategoryRequest()
        )

        return "admin/categories/form"
    }

    @PostMapping("/create")
    fun createCategory(
        @PathVariable restaurantId: Long,
        @Valid @ModelAttribute request: CreateCategoryRequest,
        bindingResult: BindingResult,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)

        if (bindingResult.hasErrors()) {
            addCategoryFormAttributes(
                model = model,
                restaurant = restaurant,
                restaurantId = restaurantId,
                request = request
            )

            return "admin/categories/form"
        }

        return try {
            categoryService.createCategory(restaurantId, request)
            "redirect:/admin/restaurants/$restaurantId/categories"
        } catch (e: IllegalArgumentException) {
            addCategoryFormAttributes(
                model = model,
                restaurant = restaurant,
                restaurantId = restaurantId,
                request = request,
                formError = e.message
            )

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

        addCategoryFormAttributes(
            model = model,
            restaurant = restaurant,
            restaurantId = restaurantId,
            categoryId = categoryId,
            request = UpdateCategoryRequest(
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
        @Valid @ModelAttribute("request") request: UpdateCategoryRequest,
        bindingResult: BindingResult,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)

        if (bindingResult.hasErrors()) {
            addCategoryFormAttributes(
                model = model,
                restaurant = restaurant,
                restaurantId = restaurantId,
                categoryId = categoryId,
                request = request
            )

            return "admin/categories/form"
        }

        return try {
            categoryService.updateCategory(restaurantId, categoryId, request)
            "redirect:/admin/restaurants/$restaurantId/categories"
        } catch (e: IllegalArgumentException) {
            addCategoryFormAttributes(
                model = model,
                restaurant = restaurant,
                restaurantId = restaurantId,
                categoryId = categoryId,
                request = request,
                formError = e.message
            )

            "admin/categories/form"
        }
    }

    private fun addCategoryFormAttributes(
        model: Model,
        restaurant: RestaurantResponse,
        restaurantId: Long,
        request: Any,
        categoryId: Long? = null,
        formError: String? = null
    ) {
        val isEdit = categoryId != null

        model.addAttribute("pageTitle", if (isEdit) "Редактировать категорию" else "Добавить категорию")
        model.addAttribute(
            "breadcrumb",
            if (isEdit) {
                "Главная / Рестораны / ${restaurant.name} / Редактировать категорию"
            } else {
                "Главная / Рестораны / ${restaurant.name} / Добавить категорию"
            }
        )
        model.addAttribute("restaurant", restaurant)
        model.addAttribute("request", request)
        model.addAttribute(
            "formAction",
            if (categoryId == null) {
                "/admin/restaurants/$restaurantId/categories/create"
            } else {
                "/admin/restaurants/$restaurantId/categories/$categoryId/edit"
            }
        )
        formError?.let { model.addAttribute("formError", it) }
    }
}