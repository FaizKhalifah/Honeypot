package com.praktikum.honeypot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.praktikum.honeypot.Navigation.AppNavHost
import com.praktikum.honeypot.Util.PreferencesHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencesHelper = PreferencesHelper(this)
        val accessToken = preferencesHelper.getAccessToken()
        val accessTokenExpiry = preferencesHelper.getAccessTokenExpiry()

        // Check if the token is expired or not found
        val startDestination = if (accessToken == null || System.currentTimeMillis() > accessTokenExpiry) {
            // Token expired or not found, navigate to login screen
            "login"
        } else {
            // Token is valid, navigate to main screen
            "main"
        }

        setContent {
            MaterialTheme {
                AppNavHost(startDestination = startDestination) // Dynamically pass start destination
            }
        }
    }
}

