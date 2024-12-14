package com.praktikum.honeypot.Screen.Sales

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.honeypot.Data.SalesUIItem
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.ViewModel.ProductViewModel
import com.praktikum.honeypot.ViewModel.SalesViewModel
import java.text.NumberFormat
import java.util.*

val dmSansFamily = FontFamily(
    Font(R.font.dmsans_regular, FontWeight.Normal),
    Font(R.font.dmsans_medium, FontWeight.Medium),
    Font(R.font.dmsans_bold, FontWeight.Bold)
)

@Composable
fun SalesScreen() {
    val context = LocalContext.current
    val salesViewModel: SalesViewModel = viewModel(factory = AppViewModelFactory(context))
    val productViewModel: ProductViewModel = viewModel(factory = AppViewModelFactory(context))
    val products by productViewModel.products.collectAsState()
    val totalStock by productViewModel.totalStock.collectAsState()
    val totalProducts = products.size
    val salesItems by salesViewModel.salesItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    spotColor = Color(0x408A959E),
                    ambientColor = Color(0x408A959E)
                )
                .width(312.dp)
                .height(80.dp)
                .background(
                    color = Color(0xFF4F9084),
                    shape = RoundedCornerShape(size = 20.dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Jenis Produk Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Jenis Produk",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = dmSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAE5)
                        ),
                        modifier = Modifier
                            .width(76.dp)
                            .height(15.dp)
                    )
                    
                    // Single horizontal line
                    Box(
                        modifier = Modifier
                            .alpha(0.4f)
                            .border(
                                width = 0.5.dp,
                                color = Color(0xFFFFFFFF)
                            )
                            .padding(0.5.dp)
                            .width(79.96877.dp)
                            .height(1.dp)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.graph),
                            contentDescription = "Graph Icon",
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                        Text(
                            text = "$totalProducts",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = dmSansFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF8FAE5)
                            ),
                            modifier = Modifier
                                .width(15.dp)
                                .height(19.dp)
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = Color(0xFFF8FAE5).copy(alpha = 0.4f))
                )

// Total Stock Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Stock",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = dmSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAE5)
                        ),
                        modifier = Modifier
                            .width(65.dp)
                            .height(15.dp)
                    )

                    // Horizontal line
                    Box(
                        modifier = Modifier
                            .alpha(0.4f)
                            .border(
                                width = 0.5.dp,
                                color = Color(0xFFFFFFFF)
                            )
                            .padding(0.5.dp)
                            .width(79.96877.dp)
                            .height(1.99922.dp)
                    )

                    // Display Total Stock
                    Text(
                        text = "$totalStock",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = dmSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAE5)
                        ),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(11.dp)
                            .height(19.dp)
                    )
                }
            }
        }

        // Tab untuk Sales dan Details
        var selectedTab by remember { mutableStateOf(0) }
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Sales") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Details") }
            )
        }
        
        // Content berdasarkan tab yang dipilih
        when (selectedTab) {
            0 -> {
                if (salesItems.isEmpty()) {
                    Text(
                        text = "No sales data available",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    SalesContent(salesItems = salesItems)
                }
            }
            1 -> {
                if (salesItems.isEmpty()) {
                    Text(
                        text = "No details available",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    DetailsContent(salesItems = salesItems)
                }
            }
        }
    }
}

@Composable
fun HeaderCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SalesContent(salesItems: List<SalesUIItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(salesItems) { item ->
            SalesItemCard(item)
        }
    }
}

@Composable
fun SalesItemCard(item: SalesUIItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Stock Tersisa: ${item.quantity}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "Pendapatan: ${formatRupiah(item.revenue)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun DetailsContent(salesItems: List<SalesUIItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(salesItems) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Detail for ${item.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    // Add more details as needed
                }
            }
        }
    }
}

fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount.toLong())
}
