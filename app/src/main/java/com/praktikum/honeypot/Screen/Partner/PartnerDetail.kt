package com.praktikum.honeypot.Screen.Partner

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Data.PartnerStock
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Screen.Home.dmSansFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetail(
    partner: Partner,
    onDismiss: () -> Unit,
    onEdit: (Partner) -> Unit,
    onDelete: (Partner) -> Unit
) {
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
                            .clickable { onDismiss() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Detail Partner",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Partner Image Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
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
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Partner Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4F9084)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = partner.name,
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.location),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = partner.address,
                                style = TextStyle(
                                    fontFamily = dmSansFontFamily,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem(
                                title = "Total Produk",
                                value = "${partner.PartnerStocks.size}",
                                icon = R.drawable.box
                            )
                            StatItem(
                                title = "Total Stok",
                                value = "${partner.PartnerStocks.sumOf { it.stock }}",
                                icon = R.drawable.graph
                            )
                        }
                    }
                }

                // Product List Title
                Text(
                    text = "Daftar Produk",
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Product List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(partner.PartnerStocks) { stock ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = stock.Product.name,
                                        style = TextStyle(
                                            fontFamily = dmSansFontFamily,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF43766C)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Stok: ${stock.stock}",
                                        style = TextStyle(
                                            fontFamily = dmSansFontFamily,
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    )
                                }
                                Icon(
                                    painter = painterResource(R.drawable.box),
                                    contentDescription = null,
                                    tint = Color(0xFF43766C),
                                    modifier = Modifier.size(24.dp)
                                )
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
                        onClick = { onDelete(partner) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Hapus",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Button(
                        onClick = { onEdit(partner) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43766C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Edit",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
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
