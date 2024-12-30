package com.praktikum.honeypot.Screen.Product

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Screen.Home.dmSansFontFamily
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    product: Product,
    onDismiss: () -> Unit,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit
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
                        text = "Detail Produk",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Product Image Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = product.image_url ?: R.drawable.placeholder_image,
                                error = painterResource(id = R.drawable.placeholder_image)
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Product Info Card
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
                            text = product.name,
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.description,
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem(
                                title = "Stok",
                                value = "${product.stock}",
                                icon = R.drawable.box
                            )
                            StatItem(
                                title = "Harga",
                                value = formatRupiah(product.price_per_unit),
                                icon = R.drawable.money
                            )
                        }
                    }
                }

                // Additional Info Card
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
                            text = "Stok produk ini dapat diatur melalui menu Partner untuk setiap mitra.",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontSize = 14.sp,
                                color = Color(0xFFF57C00)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onDelete(product) },
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
                        onClick = { onEdit(product) },
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
