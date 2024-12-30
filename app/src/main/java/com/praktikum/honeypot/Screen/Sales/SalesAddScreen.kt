package com.praktikum.honeypot.Screen.Sales

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Data.SaleRecordRequest
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.ViewModel.SalesViewModel
import com.praktikum.honeypot.ViewModel.RecordState
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesAddScreen(
    navController: NavController,
    partnerId: Int,
    viewModel: SalesViewModel = viewModel(
        factory = AppViewModelFactory(LocalContext.current)
    )
) {
    var selectedProducts by remember { mutableStateOf(listOf<Pair<Product, Int>>()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    
    val products by viewModel.availableProducts.collectAsState()
    val recordState by viewModel.recordState.collectAsState()

    // Load partner products when screen opens
    LaunchedEffect(partnerId) {
        viewModel.loadPartnerProducts(partnerId)
    }

    LaunchedEffect(recordState) {
        when (recordState) {
            is RecordState.Success -> {
                val response = (recordState as RecordState.Success).response
                if (response.errors.isEmpty()) {
                    dialogMessage = "Semua penjualan berhasil dicatat!"
                } else {
                    dialogMessage = buildString {
                        append("Beberapa penjualan gagal:\n")
                        response.errors.forEach { error ->
                            append("- ${error.message} (Stok: ${error.available_stock})\n")
                        }
                    }
                }
                showDialog = true
            }
            is RecordState.Error -> {
                dialogMessage = (recordState as RecordState.Error).message
                showDialog = true
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catat Penjualan") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Date Picker Button
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tanggal: ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product Selection
            Text(
                "Pilih Produk",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    val quantity = selectedProducts.find { it.first.product_id == product.product_id }?.second ?: 0
                    
                    ProductSelectionItem(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = { newQuantity ->
                            selectedProducts = if (newQuantity > 0) {
                                selectedProducts.toMutableList().apply {
                                    removeAll { it.first.product_id == product.product_id }
                                    add(product to newQuantity)
                                }
                            } else {
                                selectedProducts.filter { it.first.product_id != product.product_id }
                            }
                        }
                    )
                }
            }

            // Submit Button
            Button(
                onClick = {
                    val saleRecords = selectedProducts.map { (product, quantity) ->
                        SaleRecordRequest(
                            product_id = product.product_id,
                            quantity = quantity,
                            sale_date = selectedDate.toString()
                        )
                    }
                    viewModel.recordSales(partnerId, saleRecords)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = selectedProducts.isNotEmpty()
            ) {
                Text("Simpan Penjualan")
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }

        // Result Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Hasil Pencatatan") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            if (recordState is RecordState.Success && 
                                (recordState as RecordState.Success).response.errors.isEmpty()) {
                                navController.navigateUp()
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun ProductSelectionItem(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color(0xFF43766C))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Stok: ${product.stock}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Remove, "Decrease")
                }
                
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = { newValue ->
                        val newQuantity = newValue.toIntOrNull() ?: 0
                        if (newQuantity <= product.stock) {
                            onQuantityChange(newQuantity)
                        }
                    },
                    modifier = Modifier.width(80.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f),
                        focusedBorderColor = Color(0xFF43766C)
                    )
                )
                
                IconButton(
                    onClick = { if (quantity < product.stock) onQuantityChange(quantity + 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Add, "Increase")
                }
            }
        }
    }
} 