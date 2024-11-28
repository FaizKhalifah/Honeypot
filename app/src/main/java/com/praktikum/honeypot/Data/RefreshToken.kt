package com.praktikum.honeypot.Data

// For refreshing the token
data class RefreshTokenRequest(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val accessToken: String,  // New access token
    val refreshToken: String  // New refresh token (optional, if your API returns it)
)
