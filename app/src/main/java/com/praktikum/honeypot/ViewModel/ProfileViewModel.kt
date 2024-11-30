package com.praktikum.honeypot.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Owner
import com.praktikum.honeypot.Interface.ApiResponse
import com.praktikum.honeypot.Util.PreferencesHelper
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.awaitResponse

class ProfileViewModel(context: Context) : ViewModel() {
    private val profileApiService = RetrofitClient.getProfileApiService(context)
    private val preferencesHelper = PreferencesHelper(context)
    private val _profile = MutableStateFlow<Owner?>(null)
    val profile: StateFlow<Owner?> = _profile

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = profileApiService.getProfile().awaitResponse()
                if (response.isSuccessful) {
                    _profile.value = response.body()
                } else {
                    _profile.value = null
                }
            } catch (e: Exception) {
                _profile.value = null
            }
        }
    }

    fun updateProfile(
        fieldType: String,
        value: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
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
                        fetchProfile()
                        onSuccess()
                    } else {
                        onError("Failed to update: ${response.message()}")
                    }
                } catch (e: HttpException) {
                    onError("HTTP Error: ${e.message}")
                } catch (e: Exception) {
                    onError("Unexpected Error: ${e.message}")
                }
            }
        } else {
            onError("Invalid update field")
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val passwordData = mapOf(
            "currentPassword" to currentPassword,
            "newPassword" to newPassword
        )

        viewModelScope.launch {
            try {
                val response = profileApiService.changePassword(passwordData).awaitResponse()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.message == "Password updated successfully") {
                        onSuccess()
                    } else {
                        onError("Unexpected server response: ${body?.message ?: "Unknown error"}")
                    }
                } else {
                    onError("Failed to change password: ${response.message()}")
                }
            } catch (e: HttpException) {
                onError("HTTP Error: ${e.message}")
            } catch (e: Exception) {
                onError("Unexpected Error: ${e.message}")
            }
        }
    }
    fun logout(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = profileApiService.logout().awaitResponse() // Call the logout API

                if (response.isSuccessful) {
                    // Clear the stored tokens if logout is successful
                    preferencesHelper.clearTokens()
                    onSuccess()
                } else {
                    onError("Failed to log out: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Logout failed: ${e.message}")
            }
        }
    }
}
