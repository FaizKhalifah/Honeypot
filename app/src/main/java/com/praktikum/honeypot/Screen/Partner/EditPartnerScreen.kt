package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Data.PartnerStock
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Screen.Home.dmSansFontFamily
import com.praktikum.honeypot.ViewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartnerScreen(
    partnerStockList: List<PartnerStock>,
    productList: List<Product>,
    onSave: (List<PartnerStock>) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val productViewModel: ProductViewModel = viewModel(factory = AppViewModelFactory(context))
    val products by productViewModel.products.collectAsState()
    
    // Effect to refresh products when screen opens
    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    val updatedStockList = remember { mutableStateListOf(*partnerStockList.toTypedArray()) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Map untuk menyimpan input stok sementara
    val stockInputs = remember { mutableStateMapOf<Int, String>() }
    
    // Filter produk yang belum ada di partner
    val availableProducts = products.filter { product ->
        !updatedStockList.any { it.product_id == product.product_id }
    }

    // Fungsi untuk validasi input stok
    fun validateAndUpdateStock(stock: PartnerStock, newValue: String) {
        val stockChange = newValue.toIntOrNull()
        if (stockChange != null && stockChange >= 0) {
            val product = products.find { it.product_id == stock.product_id }
            if (product != null) {
                if (stockChange > product.stock) {
                    errorMessage = "Insufficient stock in the warehouse"
                    showErrorDialog = true
                    stockInputs[stock.product_id] = ""
                } else {
                    stockInputs[stock.product_id] = newValue
                }
            }
        }
    }

    // Fungsi untuk menyimpan perubahan
    fun saveChanges() {
        var hasError = false
        val newStockList = updatedStockList.map { stock ->
            val stockChange = stockInputs[stock.product_id]?.toIntOrNull() ?: 0
            val product = products.find { it.product_id == stock.product_id }
            
            if (product != null && stockChange > product.stock) {
                hasError = true
                errorMessage = "Insufficient stock in the warehouse"
                stock
            } else {
                stock.copy(stock = stockChange)
            }
        }

        if (hasError) {
            showErrorDialog = true
        } else {
            onSave(newStockList)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Logo Section with offset
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-35).dp)
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.honeypot_logo),
                    contentDescription = "Honeypot Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = (0).dp, y = (-10).dp)
                )
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .offset(y = (-60).dp)
            ) {
                // Back Button and Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onCancel() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit Stok Partner",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Box
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4F9084)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(
                            title = "Total Produk",
                            value = "${updatedStockList.size}",
                            icon = R.drawable.box
                        )
                    }
                }

                // Info Text
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "Info",
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
            Text(
                            text = "Stok hanya bisa ditambah di sini. Untuk mengurangi stok, silakan gunakan menu Report.",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontSize = 14.sp,
                                color = Color(0xFFF57C00)
                            )
                        )
                    }
                }

                // Add Product Button
                Button(
                    onClick = { showAddProductDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43766C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_circle),
                            contentDescription = "Add",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tambah Produk",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Stock List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(updatedStockList) { stock ->
                        val product = products.find { it.product_id == stock.product_id }

                        if (product != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = product.name,
                                        style = TextStyle(
                                            fontFamily = dmSansFontFamily,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF43766C)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Stok Partner: ${stock.stock}",
                                            style = TextStyle(
                                                fontFamily = dmSansFontFamily,
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                        Text(
                                            text = "Stok Gudang: ${product.stock}",
                                            style = TextStyle(
                                                fontFamily = dmSansFontFamily,
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                                        value = stockInputs[stock.product_id] ?: "",
                                        onValueChange = { newValue ->
                                            validateAndUpdateStock(stock, newValue)
                                        },
                                        label = { Text("Masukkan Stok Baru") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF43766C),
                                            unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f)
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Batal",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Button(
                        onClick = { saveChanges() },
                            modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43766C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Simpan",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Add Product Dialog
        if (showAddProductDialog) {
            AlertDialog(
                onDismissRequest = { showAddProductDialog = false },
                title = {
                    Text(
                        "Tambah Produk",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                text = {
                    LazyColumn {
                        items(availableProducts) { product ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        updatedStockList.add(
                                            PartnerStock(
                                                partner_stock_id = 0,
                                                product_id = product.product_id,
                                                partner_id = partnerStockList.firstOrNull()?.partner_id ?: 0,
                                                stock = 0,
                                                Product = product
                                            )
                                        )
                                        stockInputs[product.product_id] = ""
                                        showAddProductDialog = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = product.name,
                                            style = TextStyle(
                                                fontFamily = dmSansFontFamily,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "Stok Gudang: ${product.stock}",
                                            style = TextStyle(
                                                fontFamily = dmSansFontFamily,
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAddProductDialog = false }) {
                        Text("Tutup")
                    }
                }
            )
        }

        // Error Dialog
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = {
                    Text(
                        "Error",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                text = {
                    Text(
                        text = errorMessage,
                        style = TextStyle(
                            fontFamily = dmSansFontFamily
                        )
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun StatItem(
    title: String,
    value: String,
    icon: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = dmSansFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        
        Divider(
            modifier = Modifier
                .width(80.dp)
                .padding(vertical = 4.dp),
            color = Color.White.copy(alpha = 0.4f)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = dmSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}
