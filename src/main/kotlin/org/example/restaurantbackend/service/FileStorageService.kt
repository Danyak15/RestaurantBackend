package org.example.restaurantbackend.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class FileStorageService {
    private val restaurantUploadDir: Path = Paths.get("uploads/restaurants")
    private val dishUploadDir: Path = Paths.get("uploads/dishes")
    private val newsUploadDir: Path = Paths.get("uploads/news")
    private val userUploadDir: Path = Paths.get("uploads/users")

    init {
        Files.createDirectories(restaurantUploadDir)
        Files.createDirectories(dishUploadDir)
        Files.createDirectories(newsUploadDir)
        Files.createDirectories(userUploadDir)
    }

    fun saveUserAvatar(userId: Long, file: MultipartFile): String {
        if (file.isEmpty) throw IllegalArgumentException("Файл не должен быть пустым")
        validateImage(file)

        val extension = getExtension(file.originalFilename)
        val fileName = "user_${userId}_avatar.$extension"
        val targetPath = userUploadDir.resolve(fileName)

        file.inputStream.use { Files.copy(it, targetPath, StandardCopyOption.REPLACE_EXISTING) }
        return "/uploads/users/$fileName"
    }

    fun saveRestaurantImage(file: MultipartFile): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("Фото ресторана обязательно")
        }

        validateImage(file)

        val extension = getExtension(file.originalFilename)
        val fileName = "restaurant_${UUID.randomUUID()}.$extension"

        val targetPath = restaurantUploadDir.resolve(fileName)

        file.inputStream.use { inputStream ->
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)
        }

        return "/uploads/restaurants/$fileName"
    }

    fun replaceRestaurantImage(oldImageUrl: String?, newFile: MultipartFile?): String? {
        if (newFile == null || newFile.isEmpty) {
            return oldImageUrl
        }

        oldImageUrl?.let { deleteFileByUrl(it) }
        return saveRestaurantImage(newFile)
    }

    fun saveDishImage(file: MultipartFile): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("Фото блюда обязательно")
        }

        validateImage(file)

        val extension = getExtension(file.originalFilename)
        val fileName = "dish_${UUID.randomUUID()}.$extension"

        val targetPath = dishUploadDir.resolve(fileName)

        file.inputStream.use { inputStream ->
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)
        }

        return "/uploads/dishes/$fileName"
    }

    fun replaceDishImage(
        oldImageUrl: String?,
        newFile: MultipartFile?
    ): String? {
        if (newFile == null || newFile.isEmpty) {
            return oldImageUrl
        }

        if (!oldImageUrl.isNullOrBlank()) {
            deleteFileByUrl(oldImageUrl)
        }

        return saveDishImage(newFile)
    }

    fun saveNewsImage(file: MultipartFile): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("Фото новости обязательно")
        }

        validateImage(file)

        val extension = getExtension(file.originalFilename)
        val fileName = "news_${UUID.randomUUID()}.$extension"

        val targetPath = newsUploadDir.resolve(fileName)

        file.inputStream.use { inputStream ->
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)
        }

        return "/uploads/news/$fileName"
    }

    fun replaceNewsImage(
        oldImageUrl: String?,
        newFile: MultipartFile?
    ): String? {
        if (newFile == null || newFile.isEmpty) {
            return oldImageUrl
        }

        val newImageUrl = saveNewsImage(newFile)

        if (!oldImageUrl.isNullOrBlank()) {
            deleteFileByUrl(oldImageUrl)
        }

        return newImageUrl
    }

    fun deleteNewsImage(imageUrl: String?) {
        if (!imageUrl.isNullOrBlank()) {
            deleteFileByUrl(imageUrl)
        }
    }

    private fun validateImage(file: MultipartFile) {
        val extension = getExtension(file.originalFilename)
        val allowedExtensions = setOf("jpg", "jpeg", "png", "webp")

        if (extension !in allowedExtensions) {
            throw IllegalArgumentException("Запрещённый тип файла")
        }
    }

    private fun getExtension(originalFilename: String?): String {
        val extension = originalFilename
            ?.substringAfterLast(".", "")
            ?.lowercase()
            ?: ""

        return when (extension) {
            "jpg", "jpeg" -> "jpg"
            "png" -> "png"
            "webp" -> "webp"
            else -> "jpg"
        }
    }

    private fun deleteFileByUrl(imageUrl: String) {
        val path = when {
            imageUrl.startsWith("/uploads/restaurants/") -> {
                restaurantUploadDir.resolve(imageUrl.removePrefix("/uploads/restaurants/"))
            }

            imageUrl.startsWith("/uploads/dishes/") -> {
                dishUploadDir.resolve(imageUrl.removePrefix("/uploads/dishes/"))
            }

            imageUrl.startsWith("/uploads/news/") -> {
                newsUploadDir.resolve(imageUrl.removePrefix("/uploads/news/"))
            }

            else -> return
        }

        Files.deleteIfExists(path)
    }
}
