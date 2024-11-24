package com.praktikum.honeypot.Screen.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.ViewModel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditScreen(navController: NavController, fieldType: String, fieldValue: String) {
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory(context))
    val updatedValue = remember { mutableStateOf(fieldValue) }
    val coroutineScope = rememberCoroutineScope()

    val dmSansFont = FontFamily(
        Font(R.font.dmsans_regular),
        Font(R.font.dmsans_bold, FontWeight.Bold),
        Font(R.font.dmsans_medium, FontWeight.Medium)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Arrow
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.width(8.dp))
            // Title
            Text(
                text = "Edit ${fieldType.replaceFirstChar { it.uppercase() }}",
                fontFamily = dmSansFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable TextField
        TextField(
            value = updatedValue.value,
            onValueChange = { updatedValue.value = it },
            placeholder = { Text(text = "Enter $fieldType", fontFamily = dmSansFont) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFDF6E3), RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(55.dp)
                .background(Color(0xFF43766C), RoundedCornerShape(8.dp))
                .clickable {
                    coroutineScope.launch {
                        profileViewModel.updateProfile(
                            fieldType = fieldType,
                            value = updatedValue.value,
                            onSuccess = {
                                // Pass a refresh flag to ProfileScreen
                                navController.previousBackStackEntry?.savedStateHandle?.set("refreshProfile", true)
                                navController.popBackStack()
                            },
                            onError = { errorMessage ->
                                println("Update error: $errorMessage")
                            }
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Confirm",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = dmSansFont
            )
        }
    }
}
