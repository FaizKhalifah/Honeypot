package com.praktikum.honeypot.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Interface.ProductApiService
import com.praktikum.honeypot.Util.BitmapUtils
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductViewModel(private val context: Context) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    // Add new state for total stock
    private val _totalStock = MutableStateFlow(0)
    val totalStock: StateFlow<Int> = _totalStock

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                val productApiService = RetrofitClient.getProductApiService(context)
                val response = productApiService.getProducts()
                Log.d("ProductViewModel", "Products received: ${response.size}")
                response.forEach { product ->
                    Log.d("ProductViewModel", "Product: ${product.name}, Stock: ${product.stock}")
                }
                _products.value = response
                val total = response.sumOf { it.stock }
                Log.d("ProductViewModel", "Total stock calculated: $total")
                _totalStock.value = total
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error loading products: ${e.message}")
            }
        }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    // Function to clear selected product
    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    fun getAllProducts(): List<Product> {
        return products.value
    }

    fun addProduct(
        newProduct: Product,
        imageFile: File?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val productApiService = RetrofitClient.getProductApiService(context)

                // Prepare multipart request
                val namePart = newProduct.name.toRequestBody("text/plain".toMediaTypeOrNull())
                val descriptionPart = newProduct.description.toRequestBody("text/plain".toMediaTypeOrNull())
                val stockPart = newProduct.stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val pricePart = newProduct.price_per_unit.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart = imageFile?.let {
                    MultipartBody.Part.createFormData(
                        "image",
                        it.name,
                        it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                }

                val response = productApiService.addProduct(
                    namePart,
                    descriptionPart,
                    stockPart,
                    pricePart,
                    imagePart
                )

                if (response.isSuccessful) {
                    onSuccess()
                    loadProducts() // Refresh product list
                } else {
                    onError("Failed to add product: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
                Log.e("ProductViewModel", "Error adding product: ${e.message}")
            }
        }
    }

    fun updateProduct(
        updatedProduct: Product,
        imageFile: File?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val productApiService = RetrofitClient.getProductApiService(context)

                // Prepare multipart request
                val namePart = updatedProduct.name.toRequestBody("text/plain".toMediaTypeOrNull())
                val descriptionPart = updatedProduct.description.toRequestBody("text/plain".toMediaTypeOrNull())
                val stockPart = updatedProduct.stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val pricePart = updatedProduct.price_per_unit.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart = imageFile?.let {
                    MultipartBody.Part.createFormData(
                        "image",
                        it.name,
                        it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                }

                val response = productApiService.updateProduct(
                    updatedProduct.product_id,
                    namePart,
                    descriptionPart,
                    stockPart,
                    pricePart,
                    imagePart
                )

                if (response.isSuccessful) {
                    onSuccess()
                    loadProducts() // Refresh product list
                } else {
                    onError("Failed to update product: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
                Log.e("ProductViewModel", "Error updating product: ${e.message}")
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            try {
                val productApiService = RetrofitClient.getProductApiService(context)

                val response = productApiService.deleteProduct(productId) // API call
                if (response.isSuccessful) {
                    val currentList = products.value.toMutableList()
                    currentList.removeIf { it.product_id == productId } // Hapus dari daftar lokal
                } else {
                    Log.e("ProductViewModel", "Delete failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error deleting product: ${e.message}")
            }
        }
    }

    fun getProductById(productId: Int): Product {
        return products.value.find { it.product_id == productId }
            ?: throw IllegalArgumentException("Product not found")
    }
}
