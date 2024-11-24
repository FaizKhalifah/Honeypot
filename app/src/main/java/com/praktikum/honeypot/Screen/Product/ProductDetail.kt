package com.praktikum.honeypot.Screen.Product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.R

@Composable
fun ProductDetail(product: Product, onDismiss: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(R.drawable.sugarcane),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Text(
                text = "${product.description}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "Harga: Rp ${product.price_per_unit}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = "Stok: ${product.stock}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // Tombol kembali
            FloatingActionButton(
                onClick = { onDismiss() },
                containerColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(text = "Tutup", color = Color.Black)
            }
        }
    }
}
