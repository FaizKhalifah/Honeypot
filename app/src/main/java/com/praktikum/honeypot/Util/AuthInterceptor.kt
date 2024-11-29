package com.praktikum.honeypot.Util

import android.content.Context
import com.praktikum.honeypot.Data.RefreshTokenRequest
import com.praktikum.honeypot.Data.RefreshTokenResponse
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as RetrofitResponse
import java.util.concurrent.TimeUnit

class AuthInterceptor(private val preferencesHelper: PreferencesHelper, private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = preferencesHelper.getAccessToken()
        val accessTokenExpiry = preferencesHelper.getAccessTokenExpiry()
        val refreshToken = preferencesHelper.getRefreshToken()

        // Check if the access token is expired
        if (System.currentTimeMillis() > accessTokenExpiry && refreshToken != null) {
            // Access token expired, try refreshing it using the refresh token
            refreshAccessToken(refreshToken)
        }

        // Add the authorization header with the current access token
        val request = chain.request().newBuilder()
        if (accessToken != null) {
            request.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(request.build())
    }

    private fun refreshAccessToken(refreshToken: String) {
        val authApiService = RetrofitClient.getAuthApiService(context)
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)

        // Call the refresh token API and handle response
        val call = authApiService.refreshToken(refreshTokenRequest)
        call.enqueue(object : Callback<RefreshTokenResponse> {
            override fun onResponse(
                call: Call<RefreshTokenResponse>,
                response: RetrofitResponse<RefreshTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val newAccessToken = response.body()
                    if (newAccessToken != null) {
                        // Save the new access token and update expiry time (1 hour from now)
                        val expiryTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)
                        preferencesHelper.saveAccessToken(newAccessToken.accessToken, expiryTime)
                        preferencesHelper.saveRefreshToken(newAccessToken.refreshToken)
                    }
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                // Handle error (e.g., log it)
            }
        })
    }
}
