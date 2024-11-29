package com.praktikum.honeypot.ViewModel

import android.content.Context
import com.praktikum.honeypot.Data.LoginRequest
import com.praktikum.honeypot.Data.LoginResponse
import com.praktikum.honeypot.Data.RefreshTokenRequest
import com.praktikum.honeypot.Data.RefreshTokenResponse
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

    // Existing login function
    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val loginRequest = LoginRequest(username, password)
        val authApiService = RetrofitClient.getAuthApiService(context)

        authApiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    val refreshToken = response.body()?.refreshToken
                    if (accessToken != null && refreshToken != null) {
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

    // Refresh access token manually (to be used for forced refresh)
    fun refreshAccessToken(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val refreshToken = preferencesHelper.getRefreshToken()
        if (refreshToken != null) {
            val authApiService = RetrofitClient.getAuthApiService(context)
            val refreshTokenRequest = RefreshTokenRequest(refreshToken)

            authApiService.refreshToken(refreshTokenRequest).enqueue(object : Callback<RefreshTokenResponse> {
                override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                    if (response.isSuccessful) {
                        val newAccessToken = response.body()?.accessToken
                        val newRefreshToken = response.body()?.refreshToken
                        if (newAccessToken != null && newRefreshToken != null) {
                            val expiryTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)
                            preferencesHelper.saveAccessToken(newAccessToken, expiryTime)
                            preferencesHelper.saveRefreshToken(newRefreshToken)
                            onSuccess()
                        } else {
                            onError("Failed to refresh token")
                        }
                    } else {
                        onError("Refresh token failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                    onError("Error: ${t.message}")
                }
            })
        } else {
            onError("No refresh token available")
        }
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


