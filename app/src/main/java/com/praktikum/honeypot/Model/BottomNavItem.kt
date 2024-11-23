package com.praktikum.honeypot.Model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Home", "home", Icons.Default.Home),
    BottomNavItem("Product", "product", Icons.Default.ShoppingCart),
    BottomNavItem("Report", "report", Icons.Default.CheckCircle),
    BottomNavItem("Partner", "partner", Icons.Default.Place),
    BottomNavItem("Profile", "profile", Icons.Default.Person)
)
