package com.praktikum.honeypot.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Owner
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.await

class ProfileViewModel(context: Context) : ViewModel() {
    private val profileApiService = RetrofitClient.getProfileApiService(context)

    private val _profile = MutableStateFlow<Owner?>(null)
    val profile: StateFlow<Owner?> = _profile

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = profileApiService.getProfile().await()
                _profile.value = response
            } catch (e: Exception) {
                // Handle error
                _profile.value = null
            }
        }
    }
}
