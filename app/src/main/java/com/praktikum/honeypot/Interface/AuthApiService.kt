package com.praktikum.honeypot.Interface
import com.praktikum.honeypot.Data.LoginRequest
import com.praktikum.honeypot.Data.LoginResponse
import com.praktikum.honeypot.Data.RegisterRequest
import com.praktikum.honeypot.Data.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}
