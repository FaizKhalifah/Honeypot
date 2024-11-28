package com.praktikum.honeypot.Util

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class AuthInterceptor(private val preferencesHelper: PreferencesHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = preferencesHelper.getAccessToken() // Get the access token
        val accessTokenExpiry = preferencesHelper.getAccessTokenExpiry() // Get the access token expiry time

        // Check if token is expired
        if (System.currentTimeMillis() > accessTokenExpiry) {
            // Token expired, don't add Authorization header
            return chain.proceed(chain.request())
        }

        // Token valid, add Authorization header
        val request = chain.request().newBuilder()
        if (accessToken != null) {
            request.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(request.build())
    }
}
