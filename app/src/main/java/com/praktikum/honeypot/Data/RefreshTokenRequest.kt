package com.praktikum.honeypot.Data

import com.google.gson.annotations.SerializedName

// Request body for refreshing token
data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

// Response body for token refresh
data class RefreshTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)