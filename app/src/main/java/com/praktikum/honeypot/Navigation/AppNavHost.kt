package com.praktikum.honeypot.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.praktikum.honeypot.Screen.Profile.EditScreen
import com.praktikum.honeypot.Screen.Auth.LoginScreen
import com.praktikum.honeypot.Screen.MainScreen
import com.praktikum.honeypot.Screen.Profile.EditPasswordScreen
import com.praktikum.honeypot.Screen.Profile.ProfileScreen
import com.praktikum.honeypot.Screen.Auth.RegisterScreen
import com.praktikum.honeypot.ViewModel.AppStateViewModel

@Composable
fun AppNavHost(startDestination: String) {
    val navController = rememberNavController()
    val appStateViewModel: AppStateViewModel = viewModel()
    val isLoggedIn by appStateViewModel.isLoggedIn.observeAsState(false)
    NavHost(navController = navController, startDestination = startDestination) {
        // Login Screen
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("main") { popUpTo("login") { inclusive = true } } }
            )
        }

        // Main Screen
        composable("main") {
            MainScreen()
        }

        // Profile Screen
        composable("profile") {
            ProfileScreen(navController, appStateViewModel)  // Pass AppStateViewModel to ProfileScreen
        }
        // Register Screen
        composable("register") {
            RegisterScreen(
                onNavigateToMain = { navController.navigate("main") { popUpTo("login") { inclusive = true } } }
            )
        }
        // Edit Screen with dynamic arguments
        composable(
            "editScreen/{fieldType}/{fieldValue}",
            arguments = listOf(
                navArgument("fieldType") { type = NavType.StringType },
                navArgument("fieldValue") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fieldType = backStackEntry.arguments?.getString("fieldType") ?: ""
            val fieldValue = backStackEntry.arguments?.getString("fieldValue") ?: ""
            EditScreen(navController, fieldType, fieldValue)
        }

        // Edit Password Screen
        composable("editPasswordScreen") {
            println("Navigating to EditPasswordScreen")
            EditPasswordScreen(navController)
        }
    }
}
