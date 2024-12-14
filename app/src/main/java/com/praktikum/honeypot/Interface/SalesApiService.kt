package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.SalesResponse
import retrofit2.http.GET

interface SalesApiService {
    @GET("/api/sale/report/total")
    suspend fun getSales(): SalesResponse // Mengubah return type menjadi single SalesResponse
}
