package com.praktikum.honeypot.Util

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val preferencesHelper: PreferencesHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferencesHelper.getToken() // Ambil token dari PreferencesHelper
        val request = chain.request().newBuilder()

        if (token != null) {
            // Sisipkan token ke header Authorization
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}
