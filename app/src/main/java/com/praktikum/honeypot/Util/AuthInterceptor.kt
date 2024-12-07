package com.praktikum.honeypot.Util

import android.content.Context
import android.util.Log
import com.praktikum.honeypot.Interface.AuthApiService
import com.praktikum.honeypot.Data.RefreshTokenRequest
import com.praktikum.honeypot.Data.RefreshTokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class AuthInterceptor(
    private val preferencesHelper: PreferencesHelper,
    private val context: Context
) : Interceptor {

    // Flag to prevent multiple simultaneous refresh attempts
    private val isRefreshing = AtomicBoolean(false)
    private var newAccessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip adding token for login and register routes
        if (isLoginOrRegisterRoute(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        val accessToken = preferencesHelper.getAccessToken()

        // Add access token to the original request
        val authenticatedRequest = originalRequest.newBuilder()
            .apply {
                accessToken?.let {
                    addHeader("Authorization", "Bearer $it")
                }
            }
            .build()

        // Proceed with the authenticated request
        val response = chain.proceed(authenticatedRequest)

        // Check if the response is a 401 (Unauthorized) error
        return if (response.code == 401) {
            // Close the previous response
            response.close()

            // Synchronize token refresh
            synchronized(this) {
                val refreshToken = preferencesHelper.getRefreshToken()

                if (refreshToken != null) {
                    try {
                        // Perform token refresh
                        val refreshTokenResponse = refreshTokenSynchronously(refreshToken)

                        // Update tokens in preferences
                        val newAccessToken = refreshTokenResponse.accessToken
                        val newRefreshToken = refreshTokenResponse.refreshToken
                        val expiryTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)

                        preferencesHelper.saveAccessToken(newAccessToken, expiryTime)
                        preferencesHelper.saveRefreshToken(newRefreshToken)

                        // Retry the original request with the new access token
                        val newRequest = originalRequest.newBuilder()
                            .addHeader("Authorization", "Bearer $newAccessToken")
                            .build()

                        chain.proceed(newRequest)
                    } catch (e: Exception) {
                        // If token refresh fails, clear tokens
                        Log.e("AuthInterceptor", "Token refresh failed", e)
                        preferencesHelper.clearTokens()

                        // You might want to implement a mechanism to redirect to login screen
                        // For now, return the original 401 response
                        response
                    }
                } else {
                    // No refresh token available
                    preferencesHelper.clearTokens()
                    response
                }
            }
        } else {
            // For non-401 responses, return the original response
            response
        }
    }

    // Check if the request is for login or register routes
    private fun isLoginOrRegisterRoute(request: Request): Boolean {
        val path = request.url.encodedPath
        return path.contains("/login") || path.contains("/register") || path.contains("/refresh-token")
    }

    // Synchronous token refresh method
    private fun refreshTokenSynchronously(refreshToken: String): RefreshTokenResponse {
        // Create a separate Retrofit instance for refresh token to avoid recursive calls
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jowo-jogorogo.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authApiService = retrofit.create(AuthApiService::class.java)
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)

        // Use runBlocking to make the retrofit call synchronous
        return runBlocking {
            val response = authApiService.refreshToken(refreshTokenRequest).execute()

            if (response.isSuccessful) {
                response.body() ?: throw IOException("Empty response body")
            } else {
                throw HttpException(response)
            }
        }
    }
}