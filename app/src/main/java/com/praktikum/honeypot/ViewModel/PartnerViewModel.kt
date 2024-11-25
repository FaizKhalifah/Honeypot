package com.praktikum.honeypot.ViewModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PartnerViewModel(private val context: Context) : ViewModel() {
    private val _partners = MutableStateFlow<List<Partner>>(emptyList())
    val partners: StateFlow<List<Partner>> = _partners
    private val _selectedPartner = MutableStateFlow<Partner?>(null)
    val selectedPartner: StateFlow<Partner?> = _selectedPartner

    init {
        loadPartners()
    }

    private fun loadPartners() {
        viewModelScope.launch {
            try {
                val partnerApiService = RetrofitClient.getPartnerApiService(context)
                val response = partnerApiService.getPartner()
                _partners.value = response
            } catch (e: Exception) {
                _partners.value = emptyList() // Tangani error
            }
        }
    }

    fun selectPartner(partner: Partner) {
        _selectedPartner.value = partner
    }

    fun clearSelectedPartner() {
        _selectedPartner.value = null
    }

    fun addPartner(newPartner: Partner, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val partnerApiService = RetrofitClient.getPartnerApiService(context)
                val response = partnerApiService.addPartner(newPartner) // Tidak perlu .execute()

                if (response.isSuccessful) {
                    onSuccess()
                    loadPartners() // Refresh data produk
                } else {
                    onError("Failed to add partner: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    fun getPartnerById(partnerId: Int): Partner {
        return partners.value.find { it.partner_id == partnerId}
            ?: throw IllegalArgumentException("Partner not found")
    }

    fun updatePartner(updatedPartner: Partner) {
        viewModelScope.launch {
            val partnerApiService = RetrofitClient.getPartnerApiService(context)

            // Kirim permintaan ke API untuk memperbarui partner
            val response = partnerApiService.updatePartner(updatedPartner.partner_id, updatedPartner)
            if (response.isSuccessful) {
                // Perbarui data lokal
                _partners.value = _partners.value.map {
                    if (it.partner_id == updatedPartner.partner_id) updatedPartner else it
                }
            }
        }
    }



    fun deleteProduct(partnerId: Int) {
        viewModelScope.launch {
            try {
                val partnerApiService = RetrofitClient.getPartnerApiService(context)

                val response = partnerApiService.deletePartner(partnerId) // API call
                if (response.isSuccessful) {
                    val currentList = partners.value.toMutableList()
                    currentList.removeIf { it.partner_id == partnerId } // Hapus dari daftar lokal
                } else {
                    Log.e("PartnerViewModel", "Delete failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PartnerViewModel", "Error deleting product: ${e.message}")
            }
        }
    }

}