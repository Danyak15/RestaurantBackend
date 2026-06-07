package org.example.restaurantbackend.dto.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.example.restaurantbackend.dto.mappers.defaultRestaurantHours

data class CreateRestaurantRequest(
    @field:NotBlank(message = "Название не должно быть пустым")
    val name: String = "",

    @field:NotBlank(message = "Тип кухни не должен быть пустым")
    val cuisine: String = "",

    @field:NotBlank(message = "Адрес не должен быть пустым")
    val address: String = "",

    @field:NotBlank(message = "Описание не должно быть пустым")
    val description: String = "",

    @field:DecimalMin(value = "0.0", message = "Рейтинг не должен быть меньше 0.0")
    @field:DecimalMax(value = "5.0", message = "Рейтинг не должен быть больше 5.0")
    val rating: Double = 0.0,

    @field:Pattern(
        regexp = """^$|^\+7\d{10}$""",
        message = "Телефон должен быть РФ формата"
    )
    val phone: String? = null,

    val workingHours: MutableList<RestaurantHoursRequest> = defaultRestaurantHours()
)
