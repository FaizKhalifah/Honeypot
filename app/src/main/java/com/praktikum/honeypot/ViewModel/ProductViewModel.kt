package com.praktikum.honeypot.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val context: Context) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                val productApiService = RetrofitClient.getProductApiService(context)
                val response = productApiService.getProducts()
                _products.value = response // Atur data produk dari API di sini
            } catch (e: Exception) {
                _products.value = emptyList() // Tangani error
            }
        }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    // Fungsi untuk menghapus produk yang dipilih
    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    fun addProduct(newProduct: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val productApiService = RetrofitClient.getProductApiService(context)
                val response = productApiService.addProduct(newProduct) // Tidak perlu .execute()

                if (response.isSuccessful) {
                    onSuccess()
                    loadProducts() // Refresh data produk
                } else {
                    onError("Failed to add product: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    fun getProductById(productId: Int): Product {
        return products.value.find { it.product_id == productId }
            ?: throw IllegalArgumentException("Product not found")
    }


    fun updateProduct(updatedProduct: Product) {
        viewModelScope.launch {
            val productApiService = RetrofitClient.getProductApiService(context)

            // Kirim permintaan ke API untuk memperbarui produk
            val response = productApiService.updateProduct(updatedProduct.product_id, updatedProduct)
            if (response.isSuccessful) {
                // Perbarui data lokal
                _products.value = _products.value.map {
                    if (it.product_id == updatedProduct.product_id) updatedProduct else it
                }
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




}

