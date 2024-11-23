package com.praktikum.honeypot.Screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.praktikum.honeypot.Navigation.BottomNavigationBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen() }
            composable("product") {
                ProductScreen(
                    onNavigateToAddProduct = { navController.navigate("addProduct") }
                )
            }
            composable("addProduct") {
                AddProductScreen(
                    onProductAdded = { navController.popBackStack() }
                )
            }
            composable("report") { ReportScreen() }
            composable("partner") { PartnerScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}