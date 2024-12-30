package com.praktikum.honeypot.ViewModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Data.PartnerStock
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Data.StockUpdateRequest
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PartnerViewModel(private val context: Context) : ViewModel() {
    private val _partners = MutableStateFlow<List<Partner>>(emptyList())
    val partners: StateFlow<List<Partner>> = _partners
    private val _selectedPartner = MutableStateFlow<Partner?>(null)
    val selectedPartner: StateFlow<Partner?> = _selectedPartner

    init {
        loadPartners()
    }

    fun loadPartners() {
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

    fun addPartner(
        newPartner: Partner,
        imageFile: File?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val partnerApiService = RetrofitClient.getPartnerApiService(context)

                // Prepare multipart request
                val namePart = newPartner.name.toRequestBody("text/plain".toMediaTypeOrNull())
                val addressPart = newPartner.address.toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart = imageFile?.let {
                    MultipartBody.Part.createFormData(
                        "image",
                        it.name,
                        it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                }

                val response = partnerApiService.addPartner(
                    namePart,
                    addressPart,
                    imagePart
                )

                if (response.isSuccessful) {
                    onSuccess()
                    loadPartners() // Reload partners after adding
                } else {
                    onError("Failed to add partner")
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

    fun getPartnerStockById(partnerId: Int, productId: Int? = null): List<PartnerStock> {
        val partner = partners.value.find { it.partner_id == partnerId }
        return partner?.PartnerStocks?.filter { productId == null || it.product_id == productId } ?: emptyList()
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

    fun updatePartnerStock(partnerId: Int, productId: Int, stockChange: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val partnerApiService = RetrofitClient.getPartnerApiService(context)

                // Membuat request body untuk mengupdate stok
                val requestBody = StockUpdateRequest(product_id = productId, stockChange = stockChange)

                // Mengirim permintaan untuk memperbarui stok produk
                val response = partnerApiService.updatePartnerStock(partnerId, requestBody)

                if (response.isSuccessful) {
                    onSuccess()
                    // Setelah berhasil, kamu dapat memuat ulang data partner dan stoknya
                    loadPartners()
                } else {
                    onError("Failed to update stock: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
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