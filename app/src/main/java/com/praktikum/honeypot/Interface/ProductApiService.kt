package com.praktikum.honeypot.Interface

import com.praktikum.honeypot.Data.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {
    @GET("/api/product/")
    suspend fun getProducts(): List<Product> // Get all products

    @Multipart
    @POST("/api/product/add")
    suspend fun addProduct(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("price_per_unit") pricePerUnit: RequestBody,
        @Part image: MultipartBody.Part? // Image is optional
    ): Response<Product> // Add new product

    @Multipart
    @PUT("/api/product/update/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("price_per_unit") pricePerUnit: RequestBody,
        @Part image: MultipartBody.Part? // Image is optional
    ): Response<Product> // Update existing product

    @DELETE("/api/product/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit> // Delete product
}
