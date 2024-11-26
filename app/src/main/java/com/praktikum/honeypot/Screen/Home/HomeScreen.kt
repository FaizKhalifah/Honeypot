package com.praktikum.honeypot.Screen.Home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.ViewModel.HomeViewModel
import com.praktikum.honeypot.ViewModel.ProfileViewModel

// Define the DM Sans font family
val dmSansFontFamily = FontFamily(
    Font(resId = R.font.dmsans_bold, weight = FontWeight.Bold),
    Font(resId = R.font.dmsans_medium, weight = FontWeight.Medium),
    Font(resId = R.font.dmsans_regular, weight = FontWeight.Normal)
)

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel // Add ProfileViewModel for fetching username
) {
    val products by homeViewModel.products.collectAsState()
    val partners by homeViewModel.partners.collectAsState()
    val ownerProfile by profileViewModel.profile.collectAsState() // Observe username

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .offset(y = -40.dp), // Increased offset to move content higher
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section
        item {
            Column(
                horizontalAlignment = Alignment.Start, // Align items to the left
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp) // No padding at the top
            ) {
                // Honeypot Logo at the very top (does not move further)
                Image(
                    painter = painterResource(id = R.drawable.honeypot_logo), // Replace with your drawable logo
                    contentDescription = "Honeypot Logo",
                    modifier = Modifier
                        .size(120.dp) // Make the logo larger
                        .offset(y = 0.dp) // Negative offset to pull logo higher
                        .padding(bottom = 8.dp) // Add spacing below the logo
                )

                // Welcome Message below the logo
                Text(
                    text = "Welcome, ${ownerProfile?.username ?: "User"}!", // Display username
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.offset(y = -20.dp) // Bring the text closer to the logo
                )
            }
        }

        // Content Section (Everything else moves up by -40dp)
        item {
            OverviewCardMerged(
                titleLeft = "Jenis Produk",
                valueLeft = products.size.toString(),
                imageRes = R.drawable.graph, // Graph beside "Jenis Produk"
                titleRight = "Total Stock",
                valueRight = products.sumOf { it.stock }.toString(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Product Section
        item {
            SectionTitle(
                title = "Produk",
                onSeeAllClick = { /* Handle See All */ },
                backgroundColor = Color(0xFF43766C) // Product color
            )
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
            SectionTitle(
                title = "Partner",
                onSeeAllClick = { /* Handle See All */ },
                backgroundColor = Color(0xFF76453B) // Partner color
            )
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
fun OverviewCardMerged(
    titleLeft: String,
    valueLeft: String,
    imageRes: Int,
    titleRight: String,
    valueRight: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4F9084)), // Card background color
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Section for "Jenis Produk"
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleLeft,
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp)) // Add spacing between title and value
                // Line between title and value
                Divider(
                    color = Color.White.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(vertical = 4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Graph image on the left
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp) // Graph size 26.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Total value
                    Text(
                        text = valueLeft,
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            // Vertical Divider
            Spacer(modifier = Modifier.width(16.dp))
            Divider(
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Section for "Total Stock"
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleRight,
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp)) // Add spacing between title and value
                // Line between title and value
                Divider(
                    color = Color.White.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = valueRight,
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}



@Composable
fun SectionTitle(title: String, onSeeAllClick: () -> Unit, backgroundColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Section Title
        Text(
            text = title,
            style = TextStyle(
                fontFamily = dmSansFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        // "See All" Button with Styled Box
        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor, // Dynamically set the background color
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onSeeAllClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "See All",
                style = TextStyle(
                    fontFamily = dmSansFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}



@Composable
fun ProductCard(product: Product) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(200.dp) // Increased width
            .height(300.dp) // Increased height
            .padding(8.dp)
    ) {
        Box {
            // Image Section
            Image(
                painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxvcpxgzs/image/upload/v1679084208/samples/landscapes/architecture-signs.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            // Overlapping Stock Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 10.dp) // Positioned near the top, partially overlapping the image
                    .background(
                        color = Color(0xFF43766C), // Color for Stock badge
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Stock",
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            // Details Section Below Image
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 180.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.name,
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.money), // money.png
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Rp ${product.price_per_unit}",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 14.sp
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.box), // box.png
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.stock}",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PartnerCard(partner: Partner) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(200.dp) // Increased width
            .height(300.dp) // Increased height
            .padding(8.dp)
    ) {
        Box {
            // Image Section
            Image(
                painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxvcpxgzs/image/upload/v1679084208/samples/landscapes/architecture-signs.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            // Overlapping Partner Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 10.dp) // Positioned near the top, partially overlapping the image
                    .background(
                        color = Color(0xFF76453B), // Color for Partner badge
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Partner",
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            // Details Section Below Image
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 180.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = partner.name,
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.location), // location.png
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = partner.address,
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}



