package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.SalesResponse
import retrofit2.Response
import retrofit2.http.GET

interface SalesApiService {
    @GET("/api/sale/report/total")
    suspend fun getSales(): List<SalesResponse> // Mengambil semua data sales
}
