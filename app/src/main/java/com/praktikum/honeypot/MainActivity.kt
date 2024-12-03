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

        if (refreshToken != null) {
            if (System.currentTimeMillis() > accessTokenExpiry) {
                // Token is expired, try refreshing the access token
                authViewModel.refreshAccessToken(
                    onSuccess = {
                        // Access token refreshed successfully, proceed to main screen
                        startDestination = "main"
                        startApp(startDestination)
                    },
                    onError = { error ->
                        // Refresh token failed, clear stored tokens
                        preferencesHelper.clearTokens()
                        startDestination = "login"
                        startApp(startDestination)
                    }
                )
            } else {
                // Access token is still valid
                startDestination = "main"
                startApp(startDestination)
            }
        } else {
            // No refresh token exists, redirect to login
            startApp(startDestination)
        }
    }

    private fun startApp(startDestination: String) {
        setContent {
            MaterialTheme {
                AppNavHost(startDestination = startDestination)
            }
        }
    }
}
