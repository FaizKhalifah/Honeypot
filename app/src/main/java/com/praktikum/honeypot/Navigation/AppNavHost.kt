package com.praktikum.honeypot.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.praktikum.honeypot.Screen.LoginScreen
import com.praktikum.honeypot.Screen.MainScreen
import com.praktikum.honeypot.Screen.RegisterScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        // Halaman Login
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("main") { popUpTo("login") { inclusive = true } } }
            )
        }

        // Halaman Register
        composable("register") {
            RegisterScreen(
                onNavigateToMain = { navController.navigate("main") { popUpTo("login") { inclusive = true } } }
            )
        }

        // Halaman Utama
        composable("main") {
            MainScreen() // Implementasi halaman utama Anda
        }
    }
}

