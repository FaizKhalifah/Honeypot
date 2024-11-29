package com.praktikum.honeypot.Util

import android.content.Context
import com.praktikum.honeypot.Interface.AuthApiService
import com.praktikum.honeypot.Interface.ProductApiService
import com.praktikum.honeypot.Interface.ProfileApiService
import com.praktikum.honeypot.Interface.PartnerApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://jowo-jogorogo.vercel.app/"

    // Retrofit instance
    private lateinit var retrofit: Retrofit

    // Function to initialize Retrofit with AuthInterceptor
    private fun initRetrofit(context: Context) {
        if (!::retrofit.isInitialized) {
            val preferencesHelper = PreferencesHelper(context) // Initialize PreferencesHelper
            val authInterceptor = AuthInterceptor(preferencesHelper, context) // Add AuthInterceptor

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor) // Add interceptor to OkHttpClient
                .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
                .readTimeout(30, TimeUnit.SECONDS) // Set read timeout
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient) // Use OkHttpClient with interceptor
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    // Service instances, initialized only once
    fun getProductApiService(context: Context): ProductApiService {
        initRetrofit(context) // Initialize Retrofit
        return retrofit.create(ProductApiService::class.java)
    }

    fun getAuthApiService(context: Context): AuthApiService {
        initRetrofit(context) // Initialize Retrofit
        return retrofit.create(AuthApiService::class.java)
    }

    fun getPartnerApiService(context: Context): PartnerApiService {
        initRetrofit(context) // Initialize Retrofit
        return retrofit.create(PartnerApiService::class.java)
    }

    fun getProfileApiService(context: Context): ProfileApiService {
        initRetrofit(context) // Initialize Retrofit
        return retrofit.create(ProfileApiService::class.java)
    }
}
