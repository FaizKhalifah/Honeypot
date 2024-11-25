package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.praktikum.honeypot.Data.Partner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartnerScreen(
    partner: Partner, // Data mitra yang akan diedit
    onSave: (Partner) -> Unit, // Fungsi untuk menyimpan perubahan
    onCancel: () -> Unit // Fungsi untuk kembali tanpa menyimpan
) {
    // State untuk setiap field
    var name by remember { mutableStateOf(partner.name) }
    var address by remember { mutableStateOf(partner.address) }
    var stocks by remember { mutableStateOf(partner.PartnerStocks) } // Daftar stok produk mitra

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Mitra") },
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
            // Input nama mitra
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Mitra") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input alamat mitra
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Nama Mitra") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section untuk daftar stok
            Text(
                text = "Edit Stok Produk",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(stocks.size) { index ->
                    val stockItem = stocks[index]
                    var stockValue by remember { mutableStateOf(stockItem.stock.toString()) }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stockItem.product_id.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = stockValue,
                            onValueChange = { stockValue = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text("Stok") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Update nilai stok di daftar jika diubah
                    stocks = stocks.mapIndexed { i, item ->
                        if (i == index) item.copy(stock = stockValue.toIntOrNull() ?: item.stock) else item
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Save dan Cancel
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Validasi data sebelum menyimpan
                        val updatedPartner = partner.copy(name = name)
                        onSave(updatedPartner)
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
