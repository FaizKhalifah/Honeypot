package com.praktikum.honeypot.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Owner
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.await
import retrofit2.awaitResponse

class ProfileViewModel(context: Context) : ViewModel() {
    private val profileApiService = RetrofitClient.getProfileApiService(context)

    private val _profile = MutableStateFlow<Owner?>(null)
    val profile: StateFlow<Owner?> = _profile

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = profileApiService.getProfile().await()
                _profile.value = response
            } catch (e: Exception) {
                // Handle error (e.g., logging)
                _profile.value = null
            }
        }
    }

    fun updateProfile(fieldType: String, value: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val updateData = when (fieldType.lowercase()) {
            "fullname", "full_name" -> mapOf("full_name" to value)
            "contact" -> mapOf("contact" to value)
            "username" -> mapOf("username" to value)
            else -> null
        }

        if (updateData != null) {
            viewModelScope.launch {
                try {
                    val response = profileApiService.updateProfile(updateData).awaitResponse()
                    if (response.isSuccessful) {
                        fetchProfile() // Fetch updated profile data after a successful update
                        onSuccess()
                    } else {
                        onError("Failed to update: ${response.message()}")
                    }
                } catch (e: HttpException) {
                    onError("Error: ${e.message}")
                } catch (e: Exception) {
                    onError("Error: ${e.message}")
                }
            }
        } else {
            onError("Invalid update field")
        }
    }
}
