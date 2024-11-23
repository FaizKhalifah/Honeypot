package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.Owner
import retrofit2.Call
import retrofit2.http.GET

interface ProfileApiService {
    @GET("api/owner/profile")
    fun getProfile(): Call<Owner>
}
