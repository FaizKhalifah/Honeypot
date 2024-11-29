package com.praktikum.honeypot.Data

// Untuk login
data class LoginRequest(
    val username: String,
    val password: String
)



data class LoginResponse(
    val owner: Owner,
    val accessToken: String,   // Add the new accessToken field
    val refreshToken: String   // Add the new refreshToken field
)



// Untuk register
data class RegisterRequest(
    val username: String,
    val password: String,
    val full_name : String,
    val contact : String
)

data class RegisterResponse(
   val owner_id: Int,
   val username: String,
   val password: String,
   val full_name: String,
   val contact: String,
   val updatedAt: String,
   val createdAt: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class LogoutResponse(
    val message: String
)


