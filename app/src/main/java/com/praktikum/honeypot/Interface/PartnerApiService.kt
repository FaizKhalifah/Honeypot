package com.praktikum.honeypot.Interface
import com.praktikum.honeypot.Data.Partner
import retrofit2.Response
import retrofit2.http.*


interface PartnerApiService {
    @GET("/api/partner/")
    suspend fun getPartner(): List<Partner>

    @POST("/api/partner/add")
    suspend fun addPartner(@Body newProduct: Partner): Response<Unit>

    @PUT("/api/partner/update/{id}")
    suspend fun updatePartner(@Path("id") id: Int, @Body updatedProduct: Partner): Response<Unit>

    @DELETE("/api/partner/{id}")
    suspend fun deletePartner(@Path("id") id: Int): Response<Unit>
}