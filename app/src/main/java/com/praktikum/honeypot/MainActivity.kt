package com.praktikum.honeypot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.praktikum.honeypot.Navigation.AppNavHost
import com.praktikum.honeypot.Util.PreferencesHelper
import com.praktikum.honeypot.ViewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencesHelper = PreferencesHelper(this)
        val authViewModel = AuthViewModel(this)

        // Get tokens
        val refreshToken = preferencesHelper.getRefreshToken()
        val accessTokenExpiry = preferencesHelper.getAccessTokenExpiry()

        // Variable to determine the start destination
        var startDestination = "login"

        if (refreshToken != null && (System.currentTimeMillis() > accessTokenExpiry)) {
            // Token is expired but refresh token exists, refresh access token
            authViewModel.refreshAccessToken(
                onSuccess = {
                    // Access token refreshed successfully, set start destination to "main"
                    startDestination = "main"
                },
                onError = { error ->
                    // If refresh token fails, clear tokens and navigate to login
                    preferencesHelper.clearTokens()
                    startDestination = "login"
                }
            )
        } else if (refreshToken != null) {
            // Access token is still valid
            startDestination = "main"
        }

        setContent {
            MaterialTheme {
                AppNavHost(startDestination = startDestination) // Dynamically pass start destination
            }
        }
    }
}


