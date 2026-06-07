package org.example.restaurantbackend.controller

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.request.CreateDishRequest
import org.example.restaurantbackend.dto.request.UpdateDishRequest
import org.example.restaurantbackend.dto.response.CategoryResponse
import org.example.restaurantbackend.dto.response.RestaurantResponse
import org.example.restaurantbackend.service.CategoryService
import org.example.restaurantbackend.service.DishService
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
@RequestMapping("/admin/restaurants/{restaurantId}/categories/{categoryId}/dishes")
class AdminDishController(
    private val dishService: DishService,
    private val categoryService: CategoryService,
    private val restaurantService: RestaurantService
) {

    @GetMapping
    fun listDishes(
        @PathVariable restaurantId: Long,
        @PathVariable categoryId: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)
        val category = categoryService.getCategoryById(categoryId)

        model.addAttribute("pageTitle", "Блюда")
        model.addAttribute("breadcrumb", "Главная / Рестораны / ${restaurant.name} / ${category.name} / Блюда")
        model.addAttribute("restaurant", restaurant)
        model.addAttribute("category", category)
        model.addAttribute("dishes", dishService.getDishesByCategory(categoryId))

        return "admin/dishes/list"
    }

    @GetMapping("/create")
    fun createPage(
        @PathVariable restaurantId: Long,
        @PathVariable categoryId: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)
        val category = categoryService.getCategoryById(categoryId)

        addDishFormAttributes(
            model = model,
            restaurant = restaurant,
            category = category,
            restaurantId = restaurantId,
            categoryId = categoryId,
            request = CreateDishRequest()
        )

        return "admin/dishes/form"
    }

    @PostMapping("/create")
    fun createDish(
        @PathVariable restaurantId: Long,
        @PathVariable categoryId: Long,
        @Valid @ModelAttribute("request") request: CreateDishRequest,
        bindingResult: BindingResult,
        @RequestParam("imageFile") imageFile: MultipartFile,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)
        val category = categoryService.getCategoryById(categoryId)

        if (imageFile.isEmpty) {
            model.addAttribute("imageError", "Фото блюда обязательно")
        }

        if (bindingResult.hasErrors() || imageFile.isEmpty) {
            addDishFormAttributes(
                model = model,
                restaurant = restaurant,
                category = category,
                restaurantId = restaurantId,
                categoryId = categoryId,
                request = request
            )

            return "admin/dishes/form"
        }

        return try {
            dishService.createDish(categoryId, request, imageFile)
            "redirect:/admin/restaurants/$restaurantId/categories/$categoryId/dishes"
        } catch (e: IllegalArgumentException) {
            addDishFormAttributes(
                model = model,
                restaurant = restaurant,
                category = category,
                restaurantId = restaurantId,
                categoryId = categoryId,
                request = request,
                formError = e.message
            )

            "admin/dishes/form"
        }
    }

    @GetMapping("/{dishId}/edit")
    fun editPage(
        @PathVariable restaurantId: Long,
        @PathVariable categoryId: Long,
        @PathVariable dishId: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)
        val category = categoryService.getCategoryById(categoryId)
        val dish = dishService.getDishById(dishId)

        addDishFormAttributes(
            model = model,
            restaurant = restaurant,
            category = category,
            restaurantId = restaurantId,
            categoryId = categoryId,
            request = UpdateDishRequest(
                name = dish.name,
                price = dish.price,
                weight = dish.weight,
                description = dish.description,
                displayOrder = dish.displayOrder
            ),
            dishId = dishId,
            currentImageUrl = dish.imageUrl
        )

        return "admin/dishes/form"
    }

    @PostMapping("/{dishId}/edit")
    fun updateDish(
        @PathVariable restaurantId: Long,
        @PathVariable categoryId: Long,
        @PathVariable dishId: Long,
        @Valid @ModelAttribute("request") request: UpdateDishRequest,
        bindingResult: BindingResult,
        @RequestParam("imageFile", required = false) imageFile: MultipartFile?,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(restaurantId)
        val category = categoryService.getCategoryById(categoryId)

        if (bindingResult.hasErrors()) {
            val dish = dishService.getDishById(dishId)

            addDishFormAttributes(
                model = model,
                restaurant = restaurant,
                category = category,
                restaurantId = restaurantId,
                categoryId = categoryId,
                request = request,
                dishId = dishId,
                currentImageUrl = dish.imageUrl
            )

            return "admin/dishes/form"
        }

        return try {
            dishService.updateDish(categoryId, dishId, request, imageFile)
            "redirect:/admin/restaurants/$restaurantId/categories/$categoryId/dishes"
        } catch (e: IllegalArgumentException) {
            val dish = dishService.getDishById(dishId)

            addDishFormAttributes(
                model = model,
                restaurant = restaurant,
                category = category,
                restaurantId = restaurantId,
                categoryId = categoryId,
                request = request,
                dishId = dishId,
                currentImageUrl = dish.imageUrl,
                formError = e.message
            )

            "admin/dishes/form"
        }
    }

    private fun addDishFormAttributes(
        model: Model,
        restaurant: RestaurantResponse,
        category: CategoryResponse,
        restaurantId: Long,
        categoryId: Long,
        request: Any,
        dishId: Long? = null,
        currentImageUrl: String? = null,
        formError: String? = null
    ) {
        val isEdit = dishId != null

        model.addAttribute("pageTitle", if (isEdit) "Редактировать блюдо" else "Добавить блюдо")
        model.addAttribute(
            "breadcrumb",
            if (isEdit) {
                "Главная / Рестораны / ${restaurant.name} / ${category.name} / Редактировать блюдо"
            } else {
                "Главная / Рестораны / ${restaurant.name} / ${category.name} / Добавить блюдо"
            }
        )
        model.addAttribute("restaurant", restaurant)
        model.addAttribute("category", category)
        model.addAttribute("request", request)
        model.addAttribute("isEdit", isEdit)
        model.addAttribute(
            "formAction",
            if (dishId == null) {
                "/admin/restaurants/$restaurantId/categories/$categoryId/dishes/create"
            } else {
                "/admin/restaurants/$restaurantId/categories/$categoryId/dishes/$dishId/edit"
            }
        )
        currentImageUrl?.let { model.addAttribute("currentImageUrl", it) }
        formError?.let { model.addAttribute("formError", it) }
    }
}
