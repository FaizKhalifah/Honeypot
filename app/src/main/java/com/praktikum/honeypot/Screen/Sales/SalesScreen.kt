package com.praktikum.honeypot.Screen.Sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.honeypot.Data.SalesResponse
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.ViewModel.SalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen() {
    val context = LocalContext.current
    val salesViewModel: SalesViewModel = viewModel(factory = AppViewModelFactory(context))
    val salesResponse by salesViewModel.salesResponse.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Sales Data") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (salesResponse.isEmpty()) {
                Text(
                    text = "No sales data available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(salesResponse) { sales ->
                        SalesCard(sales)
                    }
                }
            }
        }
    }
}

@Composable
fun SalesCard(sales: SalesResponse) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Produk: ${sales.partnerSales}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Text(
                text = "quantity: ${sales.productSales}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

        }
    }
}
