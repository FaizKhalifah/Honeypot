package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
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
    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 72.dp) // Padding to avoid overlap with buttons
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Add space below the logo

            // Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_back), // Ensure the back arrow drawable exists
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                        .clickable { onDismiss() } // Close detail screen
                )
                Text(
                    text = "Detail Partner",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Partner Image
            partner.imageUrl?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            // Partner Info
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

            Spacer(modifier = Modifier.height(24.dp))

            // Product and Stock Info
            Text(
                text = "Produk dan Stok:",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Handle empty stocks
            if (partner.PartnerStocks.isNullOrEmpty()) {
                Text(
                    text = "Kosong",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                // Display partner stocks
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp) // Avoid overlap with buttons
                ) {
                    items(partner.PartnerStocks) { stock ->
                        StockItem(stock = stock)
                    }
                }
            }
        }

        // Edit and Delete Buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align to the bottom of the screen
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Edit Button
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

            // Delete Button
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
