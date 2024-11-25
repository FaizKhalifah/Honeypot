package com.praktikum.honeypot.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {
    private val productApiService = RetrofitClient.getProductApiService(context)
    private val partnerApiService = RetrofitClient.getPartnerApiService(context)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    private val _partners = MutableStateFlow<List<Partner>>(emptyList())
    val partners: StateFlow<List<Partner>> get() = _partners

    init {
        loadProducts()
        loadPartners()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                val productList = productApiService.getProducts()
                _products.value = productList
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }

    private fun loadPartners() {
        viewModelScope.launch {
            try {
                val partnerList = partnerApiService.getPartner()
                _partners.value = partnerList
            } catch (e: Exception) {
                _partners.value = emptyList()
            }
        }
    }
}
