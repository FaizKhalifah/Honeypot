package com.praktikum.honeypot.Screen.Product
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.ViewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    product: Product, // Produk yang akan diedit
    onSave: (Product) -> Unit, // Fungsi untuk menyimpan perubahan
    onCancel: () -> Unit // Fungsi untuk kembali tanpa menyimpan
) {
    // State untuk setiap field
    var name by remember { mutableStateOf(product.name) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price_per_unit.toString()) }
    var stock by remember { mutableStateOf(product.stock.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Produk") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input nama produk
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Produk") },
                modifier = Modifier.fillMaxWidth()
            )

            // Input deskripsi produk
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi Produk") },
                modifier = Modifier.fillMaxWidth()
            )

            // Input harga produk
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Harga Produk") },
                modifier = Modifier.fillMaxWidth()
            )

            // Input stok produk
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stok Produk") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tombol Save dan Cancel
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Pastikan semua data valid sebelum disimpan
                        val updatedProduct = product.copy(
                            name = name,
                            description = description,
                            price_per_unit = price.toIntOrNull() ?: 0,
                            stock = stock.toIntOrNull() ?: 0
                        )
                        onSave(updatedProduct)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("Simpan")
                }

                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Red)
                ) {
                    Text("Batal", color = Color.White)
                }
            }
        }
    }
}
