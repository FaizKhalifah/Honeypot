package com.praktikum.honeypot.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.SalesResponse
import com.praktikum.honeypot.Data.SalesUIItem
import com.praktikum.honeypot.Interface.SalesApiService
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SalesViewModel(private val context: Context) : ViewModel() {
    private val _salesResponse = MutableStateFlow<SalesResponse?>(null)
    val salesResponse: StateFlow<SalesResponse?> = _salesResponse
    
    private val _salesItems = MutableStateFlow<List<SalesUIItem>>(emptyList())
    val salesItems: StateFlow<List<SalesUIItem>> = _salesItems
    
    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems

    init {
        loadSalesData()
    }

    private fun loadSalesData() {
        viewModelScope.launch {
            try {
                val salesApiService = RetrofitClient.getSalesApiService(context)
                val response = salesApiService.getSales()
                _salesResponse.value = response
                
                // Mengkonversi response ke format UI
                val items = response.partnerSales.entries.map { (name, data) ->
                    SalesUIItem(
                        name = name,
                        quantity = data.total_quantity,
                        revenue = data.total_revenue
                    )
                }
                _salesItems.value = items
                _totalItems.value = response.overallTotal.total_quantity
                
            } catch (e: Exception) {
                Log.e("SalesViewModel", "Exception: ${e.message}")
            }
        }
    }
}
