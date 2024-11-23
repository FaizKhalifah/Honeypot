package com.praktikum.honeypot.Screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            composable("product") { ProductScreen() }
            composable("report") { ReportScreen() }
            composable("partner") { PartnerScreen() }
            composable("profile") { ProfileScreen(navController) }

            // Add editScreen route
            composable(
                route = "editScreen/{fieldType}/{fieldValue}",
                arguments = listOf(
                    navArgument("fieldType") { type = NavType.StringType },
                    navArgument("fieldValue") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val fieldType = backStackEntry.arguments?.getString("fieldType") ?: ""
                val fieldValue = backStackEntry.arguments?.getString("fieldValue") ?: ""
                EditScreen(navController, fieldType, fieldValue)
            }

        }
    }
}
