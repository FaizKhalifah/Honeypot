package com.praktikum.honeypot.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Data.SalesResponse
import com.praktikum.honeypot.Interface.SalesApiService
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SalesViewModel (private val context: Context) : ViewModel() {
    private val _salesResponse= MutableStateFlow<List<SalesResponse>>(emptyList())
    val salesResponse: StateFlow<List<SalesResponse>> = _salesResponse
    init {
        loadSalesData()
    }

    private fun loadSalesData() {
        viewModelScope.launch {
            try {
                val salesApiService = RetrofitClient.getSalesApiService(context)
                val response = salesApiService.getSales()
                _salesResponse.value=response
            } catch (e: Exception) {
                Log.e("SalesViewModel", "Exception: ${e.message}")
            }
        }
    }
}
