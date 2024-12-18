package com.praktikum.honeypot.Screen.Sales

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
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
import com.praktikum.honeypot.Data.MonthlySalesUIItem
import com.praktikum.honeypot.Data.SalesUIItem
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.ViewModel.PartnerViewModel
import com.praktikum.honeypot.ViewModel.ProductViewModel
import com.praktikum.honeypot.ViewModel.SalesViewModel
import java.text.NumberFormat
import java.util.*

val dmSansFamily = FontFamily(
    Font(R.font.dmsans_regular, FontWeight.Normal),
    Font(R.font.dmsans_medium, FontWeight.Medium),
    Font(R.font.dmsans_bold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilterSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onMonthYearSelected: (String, String) -> Unit,
    onMonthSelected: (String) -> Unit,
    onYearSelected: (String) -> Unit,
    onResetFilter: () -> Unit,
    monthlySales: List<MonthlySalesUIItem>,
    selectedMonth: String?,
    selectedYear: String?
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var showMonthMenu by remember { mutableStateOf(false) }
    var showYearMenu by remember { mutableStateOf(false) }
    var showMonthYearMenu by remember { mutableStateOf(false) }
    
    // Tambahkan state lokal untuk tracking pilihan bulan dan tahun
    var tempSelectedMonth by remember { mutableStateOf<String?>(null) }
    var tempSelectedYear by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Bar with Filter
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Cari Partner...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF43766C)
                    )
                },
                trailingIcon = {
                    Box {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = Color(0xFF43766C)
                            )
                        }

                        // Filter Type Menu
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            Text(
                                "Filter By:",
                                modifier = Modifier.padding(8.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF43766C)
                                )
                            )
                            DropdownMenuItem(
                                text = { Text("Month Only") },
                                onClick = { 
                                    showMonthMenu = true
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Year Only") },
                                onClick = { 
                                    showYearMenu = true
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Month and Year") },
                                onClick = { 
                                    showMonthYearMenu = true
                                    showFilterMenu = false
                                }
                            )
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        "Reset Filter",
                                        color = Color(0xFF43766C)
                                    )
                                },
                                onClick = {
                                    onResetFilter()
                                    showFilterMenu = false
                                }
                            )
                        }

                        // Month Only Menu
                        DropdownMenu(
                            expanded = showMonthMenu,
                            onDismissRequest = { showMonthMenu = false }
                        ) {
                            listOf(
                                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                            ).forEach { month ->
                                DropdownMenuItem(
                                    text = { Text(month) },
                                    onClick = {
                                        onMonthSelected(month)
                                        showMonthMenu = false
                                    }
                                )
                            }
                        }

                        // Year Only Menu
                        DropdownMenu(
                            expanded = showYearMenu,
                            onDismissRequest = { showYearMenu = false }
                        ) {
                            listOf(
                                "2024", "2025", "2026", "2027", "2028"
                            ).forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(year) },
                                    onClick = {
                                        onYearSelected(year)
                                        showYearMenu = false
                                    }
                                )
                            }
                        }

                        // Month and Year Menu
                        DropdownMenu(
                            expanded = showMonthYearMenu,
                            onDismissRequest = { showMonthYearMenu = false }
                        ) {
                            Text(
                                "Select Month:",
                                modifier = Modifier.padding(8.dp),
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                            listOf(
                                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                            ).forEach { month ->
                                DropdownMenuItem(
                                    text = { Text(month) },
                                    onClick = {
                                        tempSelectedMonth = month
                                        if (tempSelectedYear != null) {
                                            onMonthYearSelected(month, tempSelectedYear!!)
                                            showMonthYearMenu = false
                                        }
                                    }
                                )
                            }
                            Divider()
                            Text(
                                "Select Year:",
                                modifier = Modifier.padding(8.dp),
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                            listOf(
                                "2024", "2025", "2026", "2027", "2028"
                            ).forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(year) },
                                    onClick = {
                                        tempSelectedYear = year
                                        if (tempSelectedMonth != null) {
                                            onMonthYearSelected(tempSelectedMonth!!, year)
                                            showMonthYearMenu = false
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF43766C),
                    unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f)
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Active Filters Display
        if (selectedMonth != null || selectedYear != null || searchQuery.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Filters:",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = dmSansFamily,
                        color = Color(0xFF43766C)
                    )
                )
                if (selectedMonth != null) {
                    FilterChip(
                        selected = true,
                        onClick = { },
                        label = { Text(selectedMonth) },
                        trailingIcon = {
                            IconButton(
                                onClick = { onMonthSelected("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear month filter",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )
                }
                if (selectedYear != null) {
                    FilterChip(
                        selected = true,
                        onClick = { },
                        label = { Text(selectedYear) },
                        trailingIcon = {
                            IconButton(
                                onClick = { onYearSelected("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear year filter",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )
                }
                if (searchQuery.isNotEmpty()) {
                    FilterChip(
                        selected = true,
                        onClick = { },
                        label = { Text("Search: $searchQuery") },
                        trailingIcon = {
                            IconButton(
                                onClick = { onSearchChange("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SalesScreen(
    onNavigateToAddSales: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val salesViewModel: SalesViewModel = viewModel(factory = AppViewModelFactory(context))
    val productViewModel: ProductViewModel = viewModel(factory = AppViewModelFactory(context))
    val products by productViewModel.products.collectAsState()
    val totalStock by productViewModel.totalStock.collectAsState()
    val totalProducts = products.size
    val monthlySales by salesViewModel.monthlySales.collectAsState()

    // States for filtering and search
    var filteredSales by remember { mutableStateOf(emptyList<MonthlySalesUIItem>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf<String?>(null) }
    var selectedYear by remember { mutableStateOf<String?>(null) }
    
    // Add showPartnerDialog state
    var showPartnerDialog by remember { mutableStateOf(false) }
    val partnerViewModel: PartnerViewModel = viewModel(factory = AppViewModelFactory(context))
    val partners by partnerViewModel.partners.collectAsState()

    // Load initial data
    LaunchedEffect(Unit) {
        salesViewModel.loadSalesData()
        productViewModel.loadProducts() // Memastikan data produk dimuat
    }

    // Effect to initialize and update filtered sales
    LaunchedEffect(monthlySales) {
        filteredSales = monthlySales
    }

    // Reset filters when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            searchQuery = ""
            selectedMonth = null
            selectedYear = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Logo Section with offset
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-35).dp) // Sesuaikan dengan HomeScreen
                .height(140.dp) // Sesuaikan dengan HomeScreen
        ) {
            Image(
                painter = painterResource(id = R.drawable.honeypot_logo),
                contentDescription = "Honeypot Logo",
                modifier = Modifier
                    .size(120.dp) // Sesuaikan dengan HomeScreen
                    .align(Alignment.CenterStart)
                    .offset(x = (0).dp, y = (-20).dp), // Sesuaikan dengan HomeScreen
                contentScale = ContentScale.Fit
            )
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-60).dp) // Sesuaikan dengan HomeScreen
        ) {
            Text(
                text = "Sales Report",
                style = TextStyle(
                    fontFamily = dmSansFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

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
                    // Jenis Produk Section
                    StatItem(
                        title = "Jenis Produk",
                        value = totalProducts.toString(),
                        icon = R.drawable.graph
                    )

                    // Vertical Divider
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                            .background(Color.White.copy(alpha = 0.4f))
                    )

                    // Total Stock Section
                    StatItem(
                        title = "Total Stock",
                        value = totalStock.toString(),
                        icon = R.drawable.box
                    )
                }
            }

            // Tab Row and Content
            var selectedTab by remember { mutableStateOf(0) }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF43766C)
                ) {
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

                // Search and Filter Section with reduced vertical padding
                SearchAndFilterSection(
                    searchQuery = searchQuery,
                    onSearchChange = { 
                        searchQuery = it
                        // Update filtered sales when search changes
                        filteredSales = if (it.isEmpty() && selectedMonth == null && selectedYear == null) {
                            monthlySales
                        } else {
                            monthlySales.filter { monthData ->
                                monthData.partnerSales.any { partner ->
                                    partner.name.contains(it, ignoreCase = true)
                                }
                            }
                        }
                    },
                    onMonthYearSelected = { month, year ->
                        selectedMonth = month
                        selectedYear = year
                        // Update filtered sales for month and year
                        filteredSales = monthlySales.filter { monthData ->
                            monthData.month.contains(month) && monthData.month.endsWith(year)
                        }
                    },
                    onMonthSelected = { month ->
                        selectedMonth = month
                        selectedYear = null
                        // Update filtered sales for month only
                        filteredSales = monthlySales.filter { monthData ->
                            monthData.month.contains(month)
                        }
                    },
                    onYearSelected = { year ->
                        selectedMonth = null
                        selectedYear = year
                        // Update filtered sales for year only
                        filteredSales = monthlySales.filter { monthData ->
                            monthData.month.endsWith(year)
                        }
                    },
                    onResetFilter = {
                        selectedMonth = null
                        selectedYear = null
                        searchQuery = ""
                        filteredSales = monthlySales
                    },
                    monthlySales = monthlySales,
                    selectedMonth = selectedMonth,
                    selectedYear = selectedYear
                )

                // Content based on selected tab with reduced padding
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp)
                ) {
                    item {
                        when (selectedTab) {
                            0 -> SimpleSalesContent(filteredSales)
                            1 -> DetailedSalesContent(filteredSales)
                        }
                    }
                }
            }
        }
    }

    // Floating Action Button
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { showPartnerDialog = true },
            modifier = Modifier.padding(16.dp),
            containerColor = Color(0xFF43766C)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_circle),
                contentDescription = "Add Sales",
                tint = Color.White
            )
        }
    }

    // Partner Selection Dialog
    if (showPartnerDialog) {
        AlertDialog(
            onDismissRequest = { showPartnerDialog = false },
            title = { Text("Pilih Partner") },
            text = {
                LazyColumn {
                    items(partners) { partner ->
                        Text(
                            text = partner.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNavigateToAddSales(partner.partner_id)
                                    showPartnerDialog = false
                                }
                                .padding(vertical = 12.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPartnerDialog = false }) {
                    Text("Batal")
                }
            }
        )
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
                fontFamily = dmSansFamily,
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
                    fontFamily = dmSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun SimpleSalesContent(salesItems: List<MonthlySalesUIItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        salesItems.forEach { monthData ->
            SimpleSalesCard(monthData)
        }
    }
}

@Composable
fun SimpleSalesCard(monthData: MonthlySalesUIItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF43766C)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = monthData.month,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = dmSansFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF43766C)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            monthData.partnerSales.forEach { partner ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = partner.name,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = dmSansFamily,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = "Terjual: ${partner.quantity}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = dmSansFamily,
                                color = Color.Gray
                            )
                        )
                    }
                    Text(
                        text = formatRupiah(partner.revenue),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = dmSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43766C)
                        )
                    )
                }
                if (partner != monthData.partnerSales.last()) {
                    Divider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun DetailedSalesContent(monthlySales: List<MonthlySalesUIItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        monthlySales.forEach { monthData ->
            DetailedMonthlyCard(monthData)
        }
    }
}

@Composable
fun DetailedMonthlyCard(monthData: MonthlySalesUIItem) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF43766C)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Header dengan tombol dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthData.month,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = dmSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF43766C)
                    )
                )
                
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = Color(0xFF43766C)
                    )
                }
            }

            // Garis pemisah di bawah bulan dan tahun
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color(0xFF43766C).copy(alpha = 0.2f)
            )

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Total Penjualan
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.box),
                            contentDescription = "Sales Icon",
                            modifier = Modifier.size(24.dp),
                            colorFilter = null  // Agar warna icon tetap asli
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Total Penjualan",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = dmSansFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF43766C)
                            )
                        )
                    }
                    Text(
                        text = "${monthData.totalQuantity}",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = dmSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43766C)
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                        .background(color = Color(0xFF43766C).copy(alpha = 0.2f))
                )

                // Total Pendapatan
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.money),
                            contentDescription = "Revenue Icon",
                            modifier = Modifier.size(24.dp),
                            colorFilter = null  // Agar warna icon tetap asli
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Total Pendapatan",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = dmSansFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF43766C)
                            )
                        )
                    }
                    Text(
                        text = formatRupiah(monthData.totalRevenue),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = dmSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43766C)
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Expanded Content
            if (expanded) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = Color(0xFF43766C).copy(alpha = 0.2f)
                )
                
                // Detail Produk Section
                Text(
                    text = "Detail Produk",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = dmSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF43766C)
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                monthData.productSales.forEach { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.5.dp, Color(0xFF43766C).copy(alpha = 0.3f)),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = product.name,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = dmSansFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF43766C)
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Terjual: ${product.quantity}",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = dmSansFamily,
                                        color = Color.Gray
                                    )
                                )
                            }
                            Text(
                                text = formatRupiah(product.revenue),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = dmSansFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF43766C)
                                )
                            )
                        }
                    }
                }

                // Detail Partner Section
                Text(
                    text = "Detail Partner",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = dmSansFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF43766C)
                    ),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                
                monthData.partnerSales.forEach { partner ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.5.dp, Color(0xFF43766C).copy(alpha = 0.3f)),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = partner.name,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = dmSansFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF43766C)
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Total Penjualan: ${partner.quantity}",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = dmSansFamily,
                                        color = Color.Gray
                                    )
                                )
                            }
                            Text(
                                text = formatRupiah(partner.revenue),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = dmSansFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF43766C)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount.toLong())
}
