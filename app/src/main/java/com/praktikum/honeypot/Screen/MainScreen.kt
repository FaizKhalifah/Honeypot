package com.praktikum.honeypot.Screen

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.Navigation.BottomNavigationBar
import com.praktikum.honeypot.Screen.Auth.LoginScreen
import com.praktikum.honeypot.Screen.Auth.RegisterScreen
import com.praktikum.honeypot.Screen.Home.HomeScreen
import com.praktikum.honeypot.Screen.Partner.AddPartnerScreen
import com.praktikum.honeypot.Screen.Partner.EditPartnerScreen
import com.praktikum.honeypot.Screen.Partner.PartnerScreen
import com.praktikum.honeypot.Screen.Partner.PartnerDetail  // Import PartnerDetail
import com.praktikum.honeypot.Screen.Product.AddProductScreen
import com.praktikum.honeypot.Screen.Product.EditProductScreen
import com.praktikum.honeypot.Screen.Product.ProductDetail
import com.praktikum.honeypot.Screen.Product.ProductScreen
import com.praktikum.honeypot.Screen.Profile.EditScreen
import com.praktikum.honeypot.Screen.Profile.EditPasswordScreen
import com.praktikum.honeypot.Screen.Profile.ProfileScreen
import com.praktikum.honeypot.Screen.Report.ReportScreen
import com.praktikum.honeypot.Util.PreferencesHelper
import com.praktikum.honeypot.ViewModel.AppStateViewModel
import com.praktikum.honeypot.ViewModel.HomeViewModel
import com.praktikum.honeypot.ViewModel.PartnerViewModel
import com.praktikum.honeypot.ViewModel.ProductViewModel
import com.praktikum.honeypot.ViewModel.ProfileViewModel

@Composable
fun SplashScreen() {
    // Show a simple loading screen or splash screen while checking the token validity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()  // Show a loading spinner
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Assuming you use AppStateViewModel to track login status
    val appStateViewModel: AppStateViewModel = viewModel()
    val isLoggedIn by appStateViewModel.isLoggedIn.observeAsState(false)

    // Loading state
    var isLoading by remember { mutableStateOf(true) }

    // Get the access token from PreferencesHelper
    val preferencesHelper = PreferencesHelper(context)
    val accessToken = preferencesHelper.getAccessToken()
    val accessTokenExpiry = preferencesHelper.getAccessTokenExpiry()

    // Check if the token is expired or not
    val isTokenValid = accessToken != null && System.currentTimeMillis() <= accessTokenExpiry

    // Update login state based on token validity
    LaunchedEffect(isTokenValid) {
        if (isTokenValid) {
            appStateViewModel.logIn()
        } else {
            appStateViewModel.logOut()
        }
        // Set isLoading to false once the login state is determined
        isLoading = false
    }

    val productViewModel: ProductViewModel = viewModel(factory = AppViewModelFactory(context))
    val partnerViewModel: PartnerViewModel = viewModel(factory = AppViewModelFactory(context))
    val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelFactory(context))
    val profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory(context))

    var currentRoute by remember { mutableStateOf("") }

    // Track the current route
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route ?: ""
        }
    }

    if (isLoading) {
        // Show loading screen or splash screen while validating token
        SplashScreen()
    } else {
        Scaffold(
            bottomBar = {
                // Show BottomNavigationBar only when logged in and current route is not login/register
                if (isLoggedIn && currentRoute !in listOf("login", "register")) {
                    BottomNavigationBar(navController)
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) "home" else "login", // Conditional start destination
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home") {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        profileViewModel = profileViewModel,
                        navController = navController
                    )
                }

                composable("login") {
                    LoginScreen(
                        onNavigateToRegister = { navController.navigate("register") },
                        onLoginSuccess = {
                            appStateViewModel.logIn()  // Set login state to true
                            navController.navigate("home") {
                                popUpTo("login") {
                                    inclusive = true
                                } // Pop the login screen from the stack
                            }
                        }
                    )
                }

                composable("register") {
                    RegisterScreen(
                        onNavigateToMain = {
                            navController.navigate("home") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        navController,
                        appStateViewModel
                    )  // Pass AppStateViewModel to ProfileScreen
                }
                composable("product") {
                    ProductScreen(
                        onNavigateToAddProduct = { navController.navigate("addProduct") },
                        onNavigateToEditProduct = { product -> navController.navigate("editProduct/${product.product_id}") },
                        onDeleteProduct = { productId -> productViewModel.deleteProduct(productId) },
                        onNavigateToProductDetail = { productId -> navController.navigate("productDetail/$productId") }
                    )
                }

                composable("addProduct") {
                    AddProductScreen(
                        viewModel = productViewModel,
                        navController = navController
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

                // New route for product detail
                composable("productDetail/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                    val product = productViewModel.getProductById(productId)

                    ProductDetail(
                        product = product,
                        onDismiss = { navController.navigateUp() },
                        onEdit = { product ->
                            navController.navigate("editProduct/${product.product_id}")
                        },
                        onDelete = { product ->
                            productViewModel.deleteProduct(product.product_id)
                            navController.navigateUp()
                        }
                    )
                }

                // Partner Screens
                composable("partner") {
                    PartnerScreen(
                        onNavigateToAddPartner = { navController.navigate("addPartner") },
                        onNavigateToEditPartner = { partner ->
                            navController.navigate("editPartner/${partner.partner_id}")
                        }
                    )
                }
                composable("addPartner") {
                    AddPartnerScreen(
                        viewModel = partnerViewModel,
                        navController = navController
                    )
                }

                composable("editPartner/{partnerId}") { backStackEntry ->
                    val partnerId = backStackEntry.arguments?.getString("partnerId")?.toInt() ?: 0
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt()

                    val partner = partnerViewModel.getPartnerById(partnerId)
                    val partnerStockList =
                        partnerViewModel.getPartnerStockById(partnerId, productId)
                    val productList = productViewModel.getAllProducts()

                    EditPartnerScreen(
                        partnerStockList = partnerStockList,
                        productList = productList,
                        onSave = { updatedStockList ->
                            updatedStockList.forEach { partnerStock ->
                                partnerViewModel.updatePartnerStock(
                                    partnerId = partnerId,
                                    productId = partnerStock.product_id,
                                    stockChange = partnerStock.stock, // Asumsikan `stock` mewakili perubahan stok
                                    onSuccess = {
                                        // Berhasil diperbarui
                                        println("Stock updated for product ${partnerStock.product_id}")
                                    },
                                    onError = { errorMessage ->
                                        // Tangani error
                                        println("Failed to update stock for product ${partnerStock.product_id}: $errorMessage")
                                    }
                                )
                            }
                            navController.navigateUp()
                        },
                        onCancel = { navController.navigateUp() }
                    )
                }


                // Partner Detail Screen
                composable("partnerDetail/{partnerId}") { backStackEntry ->
                    val partnerId = backStackEntry.arguments?.getString("partnerId")?.toInt() ?: 0
                    val partner = partnerViewModel.getPartnerById(partnerId)

                    PartnerDetail(
                        partner = partner,
                        onDismiss = { navController.navigateUp() },
                        onEdit = { partner ->
                            navController.navigate("editPartner/${partner.partner_id}")
                        },
                        onDelete = { partner ->
                            partnerViewModel.deleteProduct(partner.partner_id)
                            navController.navigateUp()
                        }
                    )
                }

                // Report Screen
                composable("report") { ReportScreen() }

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
}