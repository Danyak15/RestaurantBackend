package org.example.restaurantbackend.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.example.restaurantbackend.entity.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value($$"${jwt.secret}") private val secret: String,
    @Value($$"${jwt.expiration-ms}") private val expirationMs: Long,
) {
    private val signingKey: SecretKey
        get() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun generateToken(user: UserEntity): String {
        val userId = user.id ?: throw IllegalStateException("Id пользователя null")

        val now = Date()
        val expiration = Date(now.time + expirationMs)

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(signingKey)
            .compact()
    }

    fun extractUserId(token: String): Long {
        return extractClaims(token).subject.toLong()
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            extractClaims(token)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun extractClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}