package com.praktikum.honeypot.Screen.Home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.praktikum.honeypot.Data.Partner
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.R
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
    profileViewModel: ProfileViewModel,
    navController: NavController // Pass the NavController from MainScreen
) {
    val products by homeViewModel.products.collectAsState()
    val partners by homeViewModel.partners.collectAsState()
    val ownerProfile by profileViewModel.profile.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
            .offset(y = -40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section
        item {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Honeypot Logo and Welcome message as before
                Image(
                    painter = painterResource(id = R.drawable.honeypot_logo),
                    contentDescription = "Honeypot Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "Welcome, ${ownerProfile?.username ?: "User"}!",
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.offset(y = -20.dp)
                )
            }
        }

        // Product and Partner Sections...

        // Product Section
        item {
            SectionTitle(
                title = "Produk",
                onSeeAllClick = { navController.navigate("product") },
                backgroundColor = Color(0xFF43766C)
            )
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products.size) { index ->
                    ProductCard(product = products[index], onClick = {
                        navController.navigate("productDetail/${products[index].product_id}")
                    })
                }
            }
        }

        // Partner Section
        item {
            SectionTitle(
                title = "Partner",
                onSeeAllClick = { navController.navigate("partner") },
                backgroundColor = Color(0xFF76453B)
            )
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(partners.size) { index ->
                    PartnerCard(partner = partners[index], onClick = {
                        navController.navigate("partnerDetail/${partners[index].partner_id}")
                    })
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
                .clickable { onSeeAllClick() } // Handle the click action
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
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(200.dp)
            .height(300.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)  // Navigate on click
    ){
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
fun PartnerCard(partner: Partner, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(200.dp)
            .height(300.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)  // Navigate on click
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
