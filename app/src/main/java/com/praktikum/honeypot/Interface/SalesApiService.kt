package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.SaleRecordRequest
import com.praktikum.honeypot.Data.SaleRecordResponse
import com.praktikum.honeypot.Data.SalesResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body

interface SalesApiService {
    @GET("/api/sale/report/total")
    suspend fun getSales(): SalesResponse // Mengubah return type menjadi single SalesResponse

    @POST("/api/sale/record/{partnerId}")
    suspend fun recordSales(
        @Path("partnerId") partnerId: Int,
        @Body saleRecords: List<SaleRecordRequest>
    ): SaleRecordResponse
}
