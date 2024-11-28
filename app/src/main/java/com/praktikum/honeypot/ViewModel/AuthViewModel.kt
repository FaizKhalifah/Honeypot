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
import java.util.concurrent.TimeUnit

class AuthViewModel(private val context: Context) {
    private val preferencesHelper = PreferencesHelper(context)

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val loginRequest = LoginRequest(username, password)
        val authApiService = RetrofitClient.getAuthApiService(context)

        authApiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    val refreshToken = response.body()?.refreshToken
                    if (accessToken != null && refreshToken != null) {
                        // Save access token and expiration time (1 hour from now)
                        val expiryTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)
                        preferencesHelper.saveAccessToken(accessToken, expiryTime)
                        preferencesHelper.saveRefreshToken(refreshToken)
                        onSuccess()
                    } else {
                        onError("Access token or refresh token not found in response")
                    }
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


