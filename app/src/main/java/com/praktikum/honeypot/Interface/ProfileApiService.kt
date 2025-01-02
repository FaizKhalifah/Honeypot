package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.Owner
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

data class ApiResponse(
    val message: String
)

interface ProfileApiService {
    @GET("api/owner/profile")
    fun getProfile(): Call<Owner>

    @Multipart
    @PUT("api/owner/profile")
    fun updateProfile(
        @Part("username") username: RequestBody?,
        @Part("full_name") fullName: RequestBody?,
        @Part("contact") contact: RequestBody?,
        @Part profile_image: MultipartBody.Part?
    ): Call<ApiResponse>

    @PUT("api/owner/change-password")
    fun changePassword(@Body passwordData: Map<String, String>): Call<ApiResponse>

    @POST("api/owner/logout")
    fun logout(): Call<Void>
}
