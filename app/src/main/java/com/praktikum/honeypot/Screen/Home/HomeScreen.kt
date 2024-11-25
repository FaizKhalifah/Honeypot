package com.praktikum.honeypot.Screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    homeViewModel: HomeViewModel // ViewModel for fetching products and partners
) {
    val products by homeViewModel.products.collectAsState()
    val partners by homeViewModel.partners.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section
        item {
            Text(
                text = "Welcome, owner1!",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Product and Stock Overview
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OverviewCard(
                    title = "Jenis Produk",
                    value = products.size.toString(),
                    modifier = Modifier.weight(1f)
                )
                OverviewCard(
                    title = "Total Stock",
                    value = products.sumOf { it.stock }.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Product Section
        item {
            SectionTitle("Produk", onSeeAllClick = { /* Handle See All */ })
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products.size) { index ->
                    ProductCard(product = products[index])
                }
            }
        }

        // Partner Section
        item {
            SectionTitle("Partner", onSeeAllClick = { /* Handle See All */ })
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(partners.size) { index ->
                    PartnerCard(partner = partners[index])
                }
            }
        }
    }
}

@Composable
fun OverviewCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFEAF4F4)),
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF00796B)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF00796B)
            )
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
        Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier
            .width(180.dp)
            .height(240.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxvcpxgzs/image/upload/v1679084813/image_2023-03-18_032649337_ahtnap.png"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Rp ${product.price_per_unit}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4CAF50)
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
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier
            .width(180.dp)
            .height(240.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxvcpxgzs/image/upload/v1679084208/samples/landscapes/architecture-signs.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = partner.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = partner.address,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
