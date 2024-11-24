package com.praktikum.honeypot.Interface
import com.praktikum.honeypot.Data.Product
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {
    @GET("/api/product/")
    suspend fun getProducts(): List<Product> // Mendapatkan semua stok

    @POST("/api/product/add")
    suspend fun addProduct(@Body newProduct: Product): Response<Unit> // Menambahkan stok baru

    @PUT("/api/product/update/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body updatedProduct: Product): Response<Unit> // Memperbarui stok

    @DELETE("/api/product/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>  //Delete product
}