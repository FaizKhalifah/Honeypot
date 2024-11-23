package com.praktikum.honeypot.Util

import android.content.Context
import com.praktikum.honeypot.Interface.AuthApiService
import com.praktikum.honeypot.Interface.ProductApiService
import com.praktikum.honeypot.Interface.ProfileApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://jowo-jogorogo.vercel.app/"

    // Fungsi untuk membuat Retrofit instance dengan AuthInterceptor
    fun create(context: Context): Retrofit {
        val preferencesHelper = PreferencesHelper(context) // Inisialisasi PreferencesHelper
        val authInterceptor = AuthInterceptor(preferencesHelper) // Tambahkan AuthInterceptor

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Tambahkan interceptor ke OkHttpClient
            .connectTimeout(30, TimeUnit.SECONDS) // Atur timeout
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Gunakan OkHttpClient dengan interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Service instances
    fun getProductApiService(context: Context): ProductApiService {
        return create(context).create(ProductApiService::class.java)
    }

    fun getAuthApiService(context: Context): AuthApiService {
        return create(context).create(AuthApiService::class.java)
    }
    fun getProfileApiService(context: Context): ProfileApiService {
        return create(context).create(ProfileApiService::class.java)
    }

}
