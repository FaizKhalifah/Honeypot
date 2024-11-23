package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.Owner
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApiService {
    @GET("api/owner/profile")
    fun getProfile(): Call<Owner>

    @PUT("api/owner/profile")
    fun updateProfile(@Body updateData: Map<String, String>): Call<Void>
}
