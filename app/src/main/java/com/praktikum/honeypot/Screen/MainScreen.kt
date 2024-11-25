package com.praktikum.honeypot.Screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.Navigation.BottomNavigationBar
import com.praktikum.honeypot.Screen.Home.HomeScreen
import com.praktikum.honeypot.Screen.Partner.PartnerScreen
import com.praktikum.honeypot.Screen.Product.AddProductScreen
import com.praktikum.honeypot.Screen.Product.EditProductScreen
import com.praktikum.honeypot.Screen.Product.ProductScreen
import com.praktikum.honeypot.Screen.Profile.EditScreen
import com.praktikum.honeypot.Screen.Profile.EditPasswordScreen
import com.praktikum.honeypot.Screen.Profile.ProfileScreen
import com.praktikum.honeypot.Screen.Report.ReportScreen
import com.praktikum.honeypot.ViewModel.HomeViewModel
import com.praktikum.honeypot.ViewModel.ProductViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val productViewModel: ProductViewModel = viewModel(
        factory = AppViewModelFactory(context)
    )
    val homeViewModel: HomeViewModel = viewModel(
        factory = AppViewModelFactory(context)
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(homeViewModel = homeViewModel)
            }
            composable("product") {
                ProductScreen(
                    onNavigateToAddProduct = { navController.navigate("addProduct") },
                    onNavigateToEditProduct = { product ->
                        navController.navigate("editProduct/${product.product_id}")
                    },
                    onDeleteProduct = { productId ->
                        productViewModel.deleteProduct(productId)
                    }
                )
            }
            composable("editProduct/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                val product = productViewModel.getProductById(productId)

                EditProductScreen(
                    product = product,
                    onSave = { updatedProduct ->
                        productViewModel.updateProduct(updatedProduct)
                        navController.navigateUp()
                    },
                    onCancel = { navController.navigateUp() }
                )
            }
            composable("addProduct") {
                AddProductScreen(
                    viewModel = productViewModel,
                    navController = navController
                )
            }
            composable("report") { ReportScreen() }
            composable("partner") { PartnerScreen() }
            composable("profile") { ProfileScreen(navController) }
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
            composable("editPasswordScreen") {
                EditPasswordScreen(navController)
            }
        }
    }
}
