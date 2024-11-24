package com.praktikum.honeypot.Screen.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.ViewModel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory(context))
    val profile = profileViewModel.profile.collectAsState()

    // Listen for refresh triggers from EditScreen
    val refreshState = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("refreshProfile")
        ?.observeAsState(initial = false)

    // Trigger profile fetch if refresh is true
    if (refreshState?.value == true) {
        profileViewModel.fetchProfile()
        navController.currentBackStackEntry?.savedStateHandle?.set("refreshProfile", false)
    }

    // Custom DM Sans Font
    val dmSansFont = FontFamily(
        Font(R.font.dmsans_regular),
        Font(R.font.dmsans_bold, FontWeight.Bold),
        Font(R.font.dmsans_medium, FontWeight.Medium)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Profile",
            fontFamily = dmSansFont,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture
        AsyncImage(
            model = "https://res.cloudinary.com/dxvcpxgzs/image/upload/v1679084202/samples/animals/cat.jpg",
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Owner's Name and Username
        profile.value?.let {
            Text(
                text = it.full_name,
                fontFamily = dmSansFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it.username,
                fontFamily = dmSansFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Details
        profile.value?.let {
            ProfileDetailItem(
                label = "Full Name",
                value = it.full_name,
                fontFamily = dmSansFont,
                onClick = { navController.navigate("editScreen/fullname/${it.full_name}") }
            )
            ProfileDetailItem(
                label = "Username",
                value = it.username,
                fontFamily = dmSansFont,
                onClick = { navController.navigate("editScreen/username/${it.username}") }
            )
            ProfileDetailItem(
                label = "Contact",
                value = it.contact,
                fontFamily = dmSansFont,
                onClick = { navController.navigate("editScreen/contact/${it.contact}") }
            )
            // Password Detail Box
            ProfileDetailItem(
                label = "Password",
                value = "*********", // Masked password
                fontFamily = dmSansFont,
                onClick = { /* Navigate to edit password if needed */ }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(55.dp)
                .background(Color(0xFFE84949), RoundedCornerShape(8.dp)), // Red background
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Update Profile",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = dmSansFont
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space above the navbar
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String, fontFamily: FontFamily, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(Color(0xFF43766C), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.White,
            fontFamily = fontFamily
        )
        Spacer(modifier = Modifier.weight(1f))
        // Value
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = fontFamily
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Arrow Icon
        Image(
            painter = painterResource(id = R.drawable.arrow_right),
            contentDescription = "Arrow Icon",
            modifier = Modifier.size(16.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
