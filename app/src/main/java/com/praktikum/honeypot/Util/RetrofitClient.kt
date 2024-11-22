package com.praktikum.honeypot.Util
import com.praktikum.honeypot.Interface.AuthApiService
import com.praktikum.honeypot.Interface.ProductApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://your-nodejs-server-url"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val prodcutApiService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}
