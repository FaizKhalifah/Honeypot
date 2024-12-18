package com.praktikum.honeypot.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.*
import com.praktikum.honeypot.Interface.SalesApiService
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SalesViewModel(private val context: Context) : ViewModel() {
    private val _monthlySales = MutableStateFlow<List<MonthlySalesUIItem>>(emptyList())
    val monthlySales: StateFlow<List<MonthlySalesUIItem>> = _monthlySales
    
    private val _overallTotal = MutableStateFlow<OverallTotal?>(null)
    val overallTotal: StateFlow<OverallTotal?> = _overallTotal

    init {
        loadSalesData()
    }

    private fun loadSalesData() {
        viewModelScope.launch {
            try {
                val salesApiService = RetrofitClient.getSalesApiService(context)
                val response = salesApiService.getSales()
                
                // Convert response to UI format
                val monthlyItems = response.monthlySales.map { (month, data) ->
                    MonthlySalesUIItem(
                        month = month,
                        totalQuantity = data.monthlyTotal.total_quantity,
                        totalRevenue = data.monthlyTotal.total_revenue,
                        productSales = data.productSales.map { (productName, productData) ->
                            SalesUIItem(
                                name = productName,
                                quantity = productData.total_quantity,
                                revenue = productData.total_revenue
                            )
                        },
                        partnerSales = data.partnerSales.map { (partnerName, partnerData) ->
                            SalesUIItem(
                                name = partnerName,
                                quantity = partnerData.total_quantity,
                                revenue = partnerData.total_revenue
                            )
                        }
                    )
                }.sortedByDescending { it.month }
                
                _monthlySales.value = monthlyItems
                _overallTotal.value = response.overallTotal
                
            } catch (e: Exception) {
                Log.e("SalesViewModel", "Exception: ${e.message}")
            }
        }
    }
}
