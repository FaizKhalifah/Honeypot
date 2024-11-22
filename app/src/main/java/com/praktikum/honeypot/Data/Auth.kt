package com.praktikum.honeypot.Data

// Untuk login
data class LoginRequest(
    val username: String,
    val password: String
)



data class LoginResponse(
    val owner: Owner,
    val token: String
)


// Untuk register
data class RegisterRequest(
    val name: String,
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

