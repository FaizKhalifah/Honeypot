package com.praktikum.honeypot.Screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.ViewModel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel // Add ViewModel for fetching products and partners
) {
    val products by homeViewModel.products.collectAsState()
    val partners by homeViewModel.partners.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Greeting
        Text(
            text = "Welcome, owner1!",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Product Overview
        Text(
            text = "Jenis Produk: ${products.size}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Total Stock: ${products.sumOf { it.stock }}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Product Section
        SectionTitle("Produk", onSeeAllClick = { /* Handle See All */ })
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products.size) { index ->
                ProductCard(product = products[index])
            }
        }

        // Partner Section
        SectionTitle("Partner", onSeeAllClick = { /* Handle See All */ })
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(partners.size) { index ->
                PartnerCard(partner = partners[index])
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "See All",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Rp ${product.price_per_unit}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Stock: ${product.stock}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PartnerCard(partner: Partner) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = partner.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = partner.address,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
