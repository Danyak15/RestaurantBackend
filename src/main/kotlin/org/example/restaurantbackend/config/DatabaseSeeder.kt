package org.example.restaurantbackend.config

import org.example.restaurantbackend.entity.NewsEntity
import org.example.restaurantbackend.entity.RestaurantTableEntity
import org.example.restaurantbackend.repository.NewsRepository
import org.example.restaurantbackend.repository.RestaurantTableRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DatabaseSeeder(
    private val restaurantTableRepository: RestaurantTableRepository,
    private val newsRepository: NewsRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (restaurantTableRepository.count() <= 0) {
            val tables = listOf(
                RestaurantTableEntity().apply {
                    restaurantId = 1
                    capacity = 2
                },
                RestaurantTableEntity().apply {
                    restaurantId = 1
                    capacity = 2
                },
                RestaurantTableEntity().apply {
                    restaurantId = 1
                    capacity = 3
                },
                RestaurantTableEntity().apply {
                    restaurantId = 1
                    capacity = 4
                },
                RestaurantTableEntity().apply {
                    restaurantId = 1
                    capacity = 4
                },
                RestaurantTableEntity().apply {
                    restaurantId = 1
                    capacity = 6
                },
                RestaurantTableEntity().apply {
                    restaurantId = 1
                    capacity = 8
                },

                RestaurantTableEntity().apply {
                    restaurantId = 2
                    capacity = 2
                },
                RestaurantTableEntity().apply {
                    restaurantId = 2
                    capacity = 3
                },
                RestaurantTableEntity().apply {
                    restaurantId = 2
                    capacity = 3
                },
                RestaurantTableEntity().apply {
                    restaurantId = 2
                    capacity = 4
                },
                RestaurantTableEntity().apply {
                    restaurantId = 2
                    capacity = 4
                },
                RestaurantTableEntity().apply {
                    restaurantId = 2
                    capacity = 6
                },

                RestaurantTableEntity().apply {
                    restaurantId = 3
                    capacity = 2
                },
                RestaurantTableEntity().apply {
                    restaurantId = 3
                    capacity = 4
                },
                RestaurantTableEntity().apply {
                    restaurantId = 3
                    capacity = 6
                },
                RestaurantTableEntity().apply {
                    restaurantId = 3
                    capacity = 8
                }
            )

            restaurantTableRepository.saveAll(tables)
        }

        if (newsRepository.count() <= 0) {
            val news = listOf(
                NewsEntity().apply {
                    restaurantId = 1
                    title = "Новое весеннее меню"
                    content = "Рады представить обновлённое меню с сезонными блюдами из свежих овощей и зелени."
                },
                NewsEntity().apply {
                    restaurantId = 1
                    title = "Акция: Счастливые часы"
                    createdAt = LocalDate.now().plusDays(5).toString()
                    content = "Каждый будний день с 16:00 до 18:00 скидка 20% на все закуски и напитки."
                },
                NewsEntity().apply {
                    restaurantId = 2
                    title = "Дегустация вин"
                    content = "Приглашаем на вечер итальянских вин в эту субботу. Дегустационный сет из 5 позиций."
                },
                NewsEntity().apply {
                    restaurantId = 2
                    title = "Новое детское меню"
                    createdAt = LocalDate.now().plusDays(3).toString()
                    content = "Теперь для маленьких гостей — специальное меню с полезными и вкусными блюдами."
                },
                NewsEntity().apply {
                    restaurantId = 3
                    title = "Гастроужин с шеф-поваром"
                    createdAt = LocalDate.now().plusDays(10).toString()
                    content = "7-ми блочный сет от шефа с авторскими соусами и десертом. Бронирование обязательно."
                },
                NewsEntity().apply {
                    restaurantId = 3
                    title = "Подарочные сертификаты"
                    content = "Приобретите подарочный сертификат любого номинала — отличный подарок для близких."
                }
            )

            newsRepository.saveAll(news)
        }
    }
}