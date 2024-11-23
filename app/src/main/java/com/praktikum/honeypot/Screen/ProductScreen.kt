package com.praktikum.honeypot.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.ViewModel.ProductViewModel
import androidx.compose.ui.res.painterResource
import com.praktikum.honeypot.Factory.ProductViewModelFactory
import com.praktikum.honeypot.R

@Composable
fun ProductScreen(
    onNavigateToAddProduct: () -> Unit // Fungsi navigasi untuk Add Product
) {
    val context = LocalContext.current
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(context)
    )
    val products by productViewModel.products.collectAsState()

    // Gunakan Scaffold untuk menyediakan FloatingActionButton
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToAddProduct() }, // Navigasi ke Add Product
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                // Ikon "Tambah"
                Image(
                    painter = painterResource(R.drawable.add_circle), // Pastikan ada ikon ic_add
                    contentDescription = "Add Product"
                )
            }
        }
    ) { paddingValues ->
        // LazyColumn untuk daftar produk
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product)
            }
        }
    }
}


@Composable
fun ProductCard(product: Product) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Gambar produk, bisa menggunakan gambar statis atau dari URL
            Image(
                painter = painterResource(R.drawable.sugarcane),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = product.name, color = Color.Black)
                Text(text = product.description, color = Color.Gray)
                Row() {
                    Column(modifier = Modifier.padding(1.dp, 0.dp, 60.dp, 0.dp)) {
                        Text(text = "Harga", color = Color.Gray)
                        Text(text = "Rp ${product.price_per_unit}")
                    }
                    Column {
                        Text(text = "Stok", color = Color.Gray)
                        Text(text = "${product.stock}")
                    }
                }
            }
        }
    }
}