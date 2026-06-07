package org.example.restaurantbackend.config

import org.example.restaurantbackend.service.AdminDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val adminDetailsService: AdminDetailsService
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Order(1)
    fun apiFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher("/api/**")
            .csrf { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/sync",
                        "/api/news",
                        "/api/news/**",
                        "/api/auth/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    @Order(2)
    fun adminFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/admin/login").permitAll()
                    .anyRequest().hasRole("ADMIN")
            }
            .formLogin { form ->
                form
                    .loginPage("/admin/login")
                    .usernameParameter("login")
                    .passwordParameter("password")
                    .loginProcessingUrl("/admin/login")
                    .defaultSuccessUrl("/admin/restaurants", true)
                    .failureUrl("/admin/login?error=true")
            }
            .logout { logout ->
                logout
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/admin/login")
            }
            .userDetailsService(adminDetailsService)
            .build()
    }

    @Bean
    @Order(3)
    fun staticFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher("/css/**", "/uploads/**")
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .csrf { it.disable() }
            .build()
    }
}