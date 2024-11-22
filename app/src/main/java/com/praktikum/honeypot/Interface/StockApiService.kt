package com.praktikum.honeypot.Interface
import com.praktikum.honeypot.Data.Product
import retrofit2.Call
import retrofit2.http.*

interface StockApiService {
    @GET("/api/product/")
    fun getProducts(): Call<List<Product>> // Mendapatkan semua stok

    @POST("/api/product/add")
    fun addProduct(@Body newProduct: Product): Call<Product> // Menambahkan stok baru

    @PUT("/api/product/update/{id}")
    fun updateProduct(@Path("id") id: Int, @Body updatedProduct: Product): Call<Product> // Memperbarui stok

    @DELETE("/api/product/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<Void> //Delete product
}