package com.praktikum.honeypot.ViewModel

import android.content.Context
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


}

