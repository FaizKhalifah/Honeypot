package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Data.PartnerStock
import com.praktikum.honeypot.R

@Composable
fun PartnerDetail(
    partner: Partner,
    onDismiss: () -> Unit,
    onEdit: (Partner) -> Unit,
    onDelete: (Partner) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tombol kembali (ikon panah)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_back), // Pastikan file drawable ada
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                        .clickable { onDismiss() } // Fungsi kembali
                )
                Text(
                    text = "Detail Partner",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Informasi Partner
            Text(
                text = partner.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Text(
                text = "Alamat: ${partner.address}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.padding(16.dp))

            // Daftar Stok Produk
            Text(
                text = "Produk dan Stok:",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.padding(8.dp))

            LazyColumn {
                items(partner.PartnerStocks) { stock ->
                    StockItem(stock = stock)
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            // Tombol Edit dan Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tombol Edit
                ElevatedCard(
                    onClick = { onEdit(partner) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Blue)
                ) {
                    Text(
                        text = "Edit",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Tombol Delete
                ElevatedCard(
                    onClick = { onDelete(partner) },
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.Red)
                ) {
                    Text(
                        text = "Delete",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun StockItem(stock: PartnerStock) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stock.Product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "Stok: ${stock.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}