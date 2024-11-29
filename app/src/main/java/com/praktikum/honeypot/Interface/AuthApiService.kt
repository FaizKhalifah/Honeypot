package com.praktikum.honeypot.Interface
import com.praktikum.honeypot.Data.LoginRequest
import com.praktikum.honeypot.Data.LoginResponse
import com.praktikum.honeypot.Data.RefreshTokenRequest
import com.praktikum.honeypot.Data.RefreshTokenResponse
import com.praktikum.honeypot.Data.RegisterRequest
import com.praktikum.honeypot.Data.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    // Existing login and register API calls
    @POST("api/owner/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/owner/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    // New refresh token API call
    @POST("api/owner/refresh-token")
    fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<RefreshTokenResponse>
}

