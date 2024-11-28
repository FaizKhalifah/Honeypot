package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Data.PartnerStock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartnerScreen(
    partnerStockList: List<PartnerStock>, // Daftar stok mitra yang akan diedit
    productList: List<Product>, // Daftar produk dari database
    onSave: (List<PartnerStock>) -> Unit, // Fungsi untuk menyimpan perubahan
    onCancel: () -> Unit // Fungsi untuk kembali tanpa menyimpan
) {
    // State untuk setiap stok mitra
    val updatedStockList = remember { mutableStateListOf(*partnerStockList.toTypedArray()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Stok Mitra") },
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Edit Stok Produk",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Loop melalui semua produk yang ada dalam partnerStockList
            updatedStockList.forEachIndexed { index, partnerStock ->
                val product = productList.find { it.product_id == partnerStock.product_id}
                if (product != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = product.name, modifier = Modifier.weight(1f))

                        OutlinedTextField(
                            value = partnerStock.stock.toString(),
                            onValueChange = {
                                val stockValue = it.toIntOrNull()
                                if (stockValue != null && stockValue in 0..product.stock) {
                                    updatedStockList[index] = partnerStock.copy(stock = stockValue)
                                }
                            },
                            label = { Text("Stok") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol aksi
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Batal")
                }
                Button(
                    onClick = { onSave(updatedStockList.toList()) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Simpan")
                }

            }
        }
    }
}
