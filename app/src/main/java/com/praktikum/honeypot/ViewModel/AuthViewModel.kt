package com.praktikum.honeypot.ViewModel

import android.content.Context
import com.praktikum.honeypot.Data.LoginRequest
import com.praktikum.honeypot.Data.LoginResponse
import com.praktikum.honeypot.Data.RegisterRequest
import com.praktikum.honeypot.Data.RegisterResponse
import com.praktikum.honeypot.Util.PreferencesHelper
import com.praktikum.honeypot.Util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val context: Context) {
    private val preferencesHelper = PreferencesHelper(context)

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val loginRequest = LoginRequest(username, password)
        val authApiService = RetrofitClient.getAuthApiService(context)

        authApiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    token?.let {
                        preferencesHelper.saveToken(it) // Simpan token ke PreferencesHelper
                        onSuccess()
                    } ?: onError("Token not found in response")
                } else {
                    onError("Login failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }

    fun register(
        username: String,
        password: String,
        full_name: String,
        contact: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val registerRequest = RegisterRequest(username, password, full_name, contact)
        val authApiService = RetrofitClient.getAuthApiService(context)

        authApiService.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Register failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }
}
