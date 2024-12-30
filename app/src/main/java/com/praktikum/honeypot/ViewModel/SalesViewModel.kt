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

sealed class RecordState {
    object Idle : RecordState()
    object Loading : RecordState()
    data class Success(val response: SaleRecordResponse) : RecordState()
    data class Error(val message: String) : RecordState()
}

class SalesViewModel(private val context: Context) : ViewModel() {
    private val _monthlySales = MutableStateFlow<List<MonthlySalesUIItem>>(emptyList())
    val monthlySales: StateFlow<List<MonthlySalesUIItem>> = _monthlySales
    
    private val _overallTotal = MutableStateFlow<OverallTotal?>(null)
    val overallTotal: StateFlow<OverallTotal?> = _overallTotal

    private val _recordState = MutableStateFlow<RecordState>(RecordState.Idle)
    val recordState: StateFlow<RecordState> = _recordState

    private val _availableProducts = MutableStateFlow<List<Product>>(emptyList())
    val availableProducts: StateFlow<List<Product>> = _availableProducts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun clearData() {
        _monthlySales.value = emptyList()
        _overallTotal.value = null
        _availableProducts.value = emptyList()
        _error.value = null
        _isLoading.value = false
        _recordState.value = RecordState.Idle
    }

    fun clearError() {
        _error.value = null
    }

    init {
        loadSalesData()
    }

    fun loadSalesData() {
        if (_isLoading.value) return // Prevent multiple simultaneous loads
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
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
                _error.value = "Gagal memuat data penjualan: ${e.message}"
                _monthlySales.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPartnerProducts(partnerId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val partnerApiService = RetrofitClient.getPartnerApiService(context)
                val partner = partnerApiService.getPartner().find { it.partner_id == partnerId }
                
                _availableProducts.value = partner?.PartnerStocks?.map { stock ->
                    Product(
                        product_id = stock.Product.product_id,
                        name = stock.Product.name,
                        description = stock.Product.description,
                        stock = stock.stock,
                        price_per_unit = stock.Product.price_per_unit,
                        image_url = stock.Product.image_url
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("SalesViewModel", "Error loading partner products: ${e.message}")
                _error.value = "Gagal memuat produk partner: ${e.message}"
                _availableProducts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun recordSales(partnerId: Int, saleRecords: List<SaleRecordRequest>) {
        viewModelScope.launch {
            _recordState.value = RecordState.Loading
            try {
                val salesService = RetrofitClient.getSalesApiService(context)
                val response = salesService.recordSales(partnerId, saleRecords)
                _recordState.value = RecordState.Success(response)
                // Reload sales data after successful recording
                loadSalesData()
            } catch (e: Exception) {
                _recordState.value = RecordState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun retryLoadData() {
        loadSalesData()
    }
}
