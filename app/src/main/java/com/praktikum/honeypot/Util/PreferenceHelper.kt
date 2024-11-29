package com.praktikum.honeypot.Util

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAccessToken(accessToken: String, expiryTime: Long) {
        sharedPreferences.edit().apply {
            putString("access_token", accessToken)
            putLong("access_token_expiry", expiryTime)
            apply()
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun getAccessTokenExpiry(): Long {
        return sharedPreferences.getLong("access_token_expiry", 0L)
    }

    fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refresh_token", null)
    }

    fun clearTokens() {
        sharedPreferences.edit()
            .remove("access_token")
            .remove("refresh_token")
            .apply()
    }
}
