package org.example.restaurantbackend.controller.admin

import jakarta.validation.Valid
import org.example.restaurantbackend.dto.mappers.dayNames
import org.example.restaurantbackend.dto.mappers.defaultRestaurantHours
import org.example.restaurantbackend.dto.restaurant.CreateRestaurantRequest
import org.example.restaurantbackend.dto.restaurant.RestaurantHoursRequest
import org.example.restaurantbackend.dto.restaurant.UpdateRestaurantRequest
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
        addRestaurantFormAttributes(
            model = model,
            request = CreateRestaurantRequest()
        )

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
            addRestaurantFormAttributes(
                model = model,
                request = request
            )

            return "admin/restaurants/form"
        }

        return try {
            restaurantService.createRestaurant(request, imageFile)
            "redirect:/admin/restaurants"
        } catch (e: IllegalArgumentException) {
            addRestaurantFormAttributes(
                model = model,
                request = request,
                formError = e.message
            )

            "admin/restaurants/form"
        }
    }

    @GetMapping("/{id}/edit")
    fun editPage(
        @PathVariable id: Long,
        model: Model
    ): String {
        val restaurant = restaurantService.getRestaurantById(id)

        addRestaurantFormAttributes(
            model = model,
            request = UpdateRestaurantRequest(
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
            ),
            id = id,
            currentImageUrl = restaurant.imageUrl
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
            val restaurant = restaurantService.getRestaurantById(id)

            addRestaurantFormAttributes(
                model = model,
                request = request,
                id = id,
                currentImageUrl = restaurant.imageUrl
            )

            return "admin/restaurants/form"
        }

        restaurantService.updateRestaurant(id, request, imageFile)

        return "redirect:/admin/restaurants"
    }

    private fun addRestaurantFormAttributes(
        model: Model,
        request: Any,
        id: Long? = null,
        currentImageUrl: String? = null,
        formError: String? = null
    ) {
        val isEdit = id != null

        model.addAttribute("pageTitle", if (isEdit) "Редактировать ресторан" else "Добавить ресторан")
        model.addAttribute(
            "breadcrumb",
            if (isEdit) {
                "Главная / Рестораны / Редактировать"
            } else {
                "Главная / Рестораны / Добавить"
            }
        )
        model.addAttribute("formAction", if (id == null) "/admin/restaurants/create" else "/admin/restaurants/$id/edit")
        model.addAttribute("request", request)
        model.addAttribute("isEdit", isEdit)
        model.addAttribute("dayNames", dayNames())
        currentImageUrl?.let { model.addAttribute("currentImageUrl", it) }
        formError?.let { model.addAttribute("formError", it) }
    }
}