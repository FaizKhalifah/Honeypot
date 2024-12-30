package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Screen.Home.dmSansFontFamily
import com.praktikum.honeypot.ViewModel.PartnerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerScreen(
    navController: NavController,
    onNavigateToAddPartner: () -> Unit,
    onNavigateToEditPartner: (Partner) -> Unit,
) {
    val context = LocalContext.current
    val partnerViewModel: PartnerViewModel = viewModel(factory = AppViewModelFactory(context))
    val partners by partnerViewModel.partners.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<Partner?>(null) }

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
                        .offset(x = (0).dp, y = (-10).dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .offset(y = (-60).dp)
            ) {
                Text(
                    text = "Partner Management",
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
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
                        // Total Partner Section
                        StatItem(
                            title = "Total Partner",
                            value = partners.size.toString(),
                            icon = R.drawable.location
                        )

                        // Vertical Divider
                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp)
                                .background(Color.White.copy(alpha = 0.4f))
                        )

                        // Total Produk Section
                        StatItem(
                            title = "Total Produk",
                            value = partners.sumOf { it.PartnerStocks.size }.toString(),
                            icon = R.drawable.box
                        )
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    placeholder = { Text("Cari Partner...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF43766C)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF43766C),
                        unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Partner List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        partners.filter { partner ->
                            partner.name.contains(searchQuery, ignoreCase = true)
                        }
                    ) { partner ->
                        PartnerListItem(
                            partner = partner,
                            onEditClick = { onNavigateToEditPartner(partner) },
                            onDeleteClick = { showDeleteDialog = partner },
                            onClick = { navController.navigate("partnerDetail/${partner.partner_id}") }
                        )
                    }
                }
            }
        }

        // FAB for adding new partner
        FloatingActionButton(
            onClick = onNavigateToAddPartner,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF43766C)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_circle),
                contentDescription = "Add Partner",
                tint = Color.White
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Apakah Anda yakin ingin menghapus partner ${showDeleteDialog?.name}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog?.let {
                                partnerViewModel.deleteProduct(it.partner_id)
                            }
                            showDeleteDialog = null
                        }
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Batal")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerListItem(
    partner: Partner,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF43766C)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Partner Image and Details
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Partner Image
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = partner.imageUrl ?: R.drawable.placeholder_image,
                            error = painterResource(id = R.drawable.placeholder_image)
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Partner Details
                Column {
                    Text(
                        text = partner.name,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = dmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43766C)
                        )
                    )
                    Text(
                        text = partner.address,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = dmSansFontFamily,
                            color = Color.Gray
                        )
                    )
                    Text(
                        text = "Total Produk: ${partner.PartnerStocks.size}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = dmSansFontFamily,
                            color = Color.Gray
                        )
                    )
                }
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF43766C)
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFF43766C)
                    )
                }
            }
        }
    }
}
