package com.praktikum.honeypot.Screen.Product

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.praktikum.honeypot.Data.Product
import androidx.compose.ui.res.painterResource
import com.praktikum.honeypot.R

@Composable
fun ProductDetail(
    product: Product,
    onDismiss: () -> Unit,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Back button and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_back), // Ensure drawable exists
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                        .clickable { onDismiss() } // Back function
                )
                Text(
                    text = "Detail Produk",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Image
            if (product.image_url != null) {
                AsyncImage(
                    model = product.image_url,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.placeholder_image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Product Information
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Text(
                text = "Deskripsi: ${product.description}",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Edit and Delete Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Edit Button
                ElevatedCard(
                    onClick = { onEdit(product) },
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
                    onClick = { onDelete(product) },
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
