package com.praktikum.honeypot.Util

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val preferencesHelper: PreferencesHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferencesHelper.getToken() // Get token from preferences
        val request = chain.request().newBuilder()

        if (token != null) {
            // Attach token to the Authorization header
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}

