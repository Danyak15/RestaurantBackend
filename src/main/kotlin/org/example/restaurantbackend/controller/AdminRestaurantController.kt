package org.example.restaurantbackend.controller

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.mappers.dayNames
import org.example.restaurantbackend.dto.mappers.defaultRestaurantHours
import org.example.restaurantbackend.dto.request.CreateRestaurantRequest
import org.example.restaurantbackend.dto.request.RestaurantHoursRequest
import org.example.restaurantbackend.dto.request.UpdateRestaurantRequest
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
@RequestMapping("/admin/restaurants")
class AdminRestaurantController(
    private val restaurantService: RestaurantService
) {
    @GetMapping
    fun listRestaurants(model: Model): String {
        model.addAttribute("pageTitle", "Рестораны")
        model.addAttribute("breadcrumb", "Главная / Рестораны")
        model.addAttribute("restaurants", restaurantService.getRestaurants())

        return "admin/restaurants/list"
    }

    @GetMapping("/create")
    fun createPage(model: Model): String {
        model.addAttribute("pageTitle", "Добавить ресторан")
        model.addAttribute("breadcrumb", "Главная / Рестораны / Добавить")
        model.addAttribute("formAction", "/admin/restaurants/create")
        model.addAttribute("request", CreateRestaurantRequest())
        model.addAttribute("isEdit", false)
        model.addAttribute("dayNames", dayNames())

        return "admin/restaurants/form"
    }

    @PostMapping("/create")
    fun createRestaurant(
        @Valid @ModelAttribute request: CreateRestaurantRequest,
        bindingResult: BindingResult,
        @RequestParam imageFile: MultipartFile,
        model: Model
    ): String {
        if (imageFile.isEmpty) {
            bindingResult.rejectValue("name", "image.required", "Фото ресторана обязательно")
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Добавить ресторан")
            model.addAttribute("breadcrumb", "Главная / Рестораны / Добавить")
            model.addAttribute("formAction", "/admin/restaurants/create")
            model.addAttribute("isEdit", false)


            return "admin/restaurants/form"
        }

        return try {
            restaurantService.createRestaurant(request, imageFile)
            "redirect:/admin/restaurants"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("pageTitle", "Добавить ресторан")
            model.addAttribute("breadcrumb", "Главная / Рестораны / Добавить")
            model.addAttribute("formAction", "/admin/restaurants/create")
            model.addAttribute("isEdit", false)
            model.addAttribute("formError", e.message)

            "admin/restaurants/form"
        }
    }

    @GetMapping("/{id}/edit")
    fun editPage(
        @PathVariable id: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(id)

        model.addAttribute("pageTitle", "Редактировать ресторан")
        model.addAttribute("breadcrumb", "Главная / Рестораны / Редактировать")
        model.addAttribute("formAction", "/admin/restaurants/$id/edit")
        model.addAttribute("isEdit", true)
        model.addAttribute("dayNames", dayNames())
        model.addAttribute("currentImageUrl", restaurant.imageUrl)

        model.addAttribute(
            "request",
            UpdateRestaurantRequest(
                name = restaurant.name,
                cuisine = restaurant.cuisine,
                address = restaurant.address,
                description = restaurant.description,
                rating = restaurant.rating,
                phone = restaurant.phone,
                workingHours = restaurant.workingHours
                    .sortedBy { it.dayOfWeek.value }
                    .map {
                        RestaurantHoursRequest(
                            dayOfWeek = it.dayOfWeek,
                            isClosed = it.isClosed,
                            openTime = it.openTime,
                            closeTime = it.closeTime
                        )
                    }
                    .ifEmpty {
                        defaultRestaurantHours()
                    }
                    .toMutableList()
            )
        )

        return "admin/restaurants/form"
    }

    @PostMapping("/{id}/edit")
    fun updateRestaurant(
        @PathVariable id: Long,
        @Valid @ModelAttribute("request") request: UpdateRestaurantRequest,
        bindingResult: BindingResult,
        @RequestParam(required = false) imageFile: MultipartFile?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Редактировать ресторан")
            model.addAttribute("breadcrumb", "Главная / Рестораны / Редактировать")
            model.addAttribute("formAction", "/admin/restaurants/$id/edit")
            model.addAttribute("isEdit", true)
            model.addAttribute("currentImageUrl", restaurantService.getRestaurantById(id).imageUrl)

            return "admin/restaurants/form"
        }

        restaurantService.updateRestaurant(id, request, imageFile)

        return "redirect:/admin/restaurants"
    }
}