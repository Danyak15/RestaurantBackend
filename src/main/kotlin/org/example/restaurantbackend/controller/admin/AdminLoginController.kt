package org.example.restaurantbackend.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminLoginController {
    @GetMapping("/admin/login")
    fun loginPage(): String {
        return "admin-login"
    }
}