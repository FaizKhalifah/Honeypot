package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.Owner
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

data class ApiResponse(
    val message: String // Add this to capture the API response message
)

interface ProfileApiService {
    @GET("api/owner/profile")
    fun getProfile(): Call<Owner>

    @PUT("api/owner/profile")
    fun updateProfile(@Body updateData: Map<String, String>): Call<Void>

    @PUT("api/owner/change-password")
    fun changePassword(@Body passwordData: Map<String, String>): Call<ApiResponse>
}
