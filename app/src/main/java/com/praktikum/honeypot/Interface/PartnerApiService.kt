package com.praktikum.honeypot.Interface
import com.praktikum.honeypot.Data.Product
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface PartnerApiService {
    @GET("/api/partner/")
    suspend fun getPartner(): List<Product>

    @POST("/api/partner/add")
    suspend fun addPartner(@Body newProduct: Product): Response<Unit>

    @PUT("/api/partner/update/{id}")
    suspend fun updatePartner(@Path("id") id: Int, @Body updatedProduct: Product): Response<Unit>

    @DELETE("/api/partner/{id}")
    suspend fun deletePartner(@Path("id") id: Int): Response<Unit>
}