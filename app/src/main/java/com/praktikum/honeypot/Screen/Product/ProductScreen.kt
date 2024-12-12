package com.praktikum.honeypot.Screen.Product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.ViewModel.ProductViewModel
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
@Composable
fun ProductScreen(
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (Product) -> Unit,
    onDeleteProduct: (Int) -> Unit,
    onNavigateToProductDetail: (Int) -> Unit
) {
    val context = LocalContext.current
    val productViewModel: ProductViewModel = viewModel(
        factory = AppViewModelFactory(context)
    )
    val products by productViewModel.products.collectAsState()
    val selectedProduct by productViewModel.selectedProduct.collectAsState()

    // State for search bar
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToAddProduct() },
                containerColor = Color.White,
                contentColor = Color.White
            ) {
                Image(
                    painter = painterResource(R.drawable.add_circle),
                    contentDescription = "Add Product"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize() // Ensure the Box takes the full available space
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = -90.dp) // Keeps the logo offset from top
            ) {
                // Logo positioned top-left with offset
                Image(
                    painter = painterResource(id = R.drawable.honeypot_logo),
                    contentDescription = "Honeypot Logo",
                    modifier = Modifier
                        .size(125.dp)
                        .padding(start = 16.dp, top = 16.dp) // Offset logo from the top-left corner
                )

                // Add search bar just below the logo
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )

                Spacer(modifier = Modifier.height(16.dp)) // Spacer between search and products
            }

            // LazyColumn to display the product cards and fill remaining space
            val filteredProducts = products.filter { product ->
                product.name.contains(searchText, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 150.dp) // Adjust to give space for logo and search bar
                    .fillMaxSize() // Ensure this takes all the remaining space
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onNavigateToProductDetail(product.product_id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick, // Click function
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth() // Full width for product card
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Product image
            if (product.image_url != null) {
                AsyncImage(
                    model = product.image_url,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(150.dp) // Larger size for image
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.placeholder_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp) // Larger placeholder size
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Product text information
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = product.name,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = product.description,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    // Price Column
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Price",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Rp ${product.price_per_unit}",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Stock Column
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Stock",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${product.stock}",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    androidx.compose.material3.TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text(text = "Search product...") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
            containerColor = Color.White
        )
    )
}



