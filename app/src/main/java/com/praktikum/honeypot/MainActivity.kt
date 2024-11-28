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

        val startDestination = if (System.currentTimeMillis() > accessTokenExpiry || accessToken == null) {
            // Token expired or not found, navigate to login screen
            "login"
        } else {
            // Token valid, navigate to main screen
            "main"
        }

        setContent {
            MaterialTheme {
                AppNavHost(startDestination = startDestination) // Pass dynamic start destination
            }
        }
    }
}
