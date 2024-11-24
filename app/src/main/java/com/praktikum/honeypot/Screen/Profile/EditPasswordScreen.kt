package com.praktikum.honeypot.Screen.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.ViewModel.ProfileViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun EditPasswordScreen(navController: NavController) {
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory(context))
    val coroutineScope = rememberCoroutineScope()

    val oldPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

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
        // Back Arrow and Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )

            Text(
                text = "Edit Password",
                fontFamily = dmSansFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Old Password TextField
        PasswordTextField(
            label = "Old Password",
            value = oldPassword.value,
            onValueChange = { oldPassword.value = it },
            dmSansFont = dmSansFont
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New Password TextField
        PasswordTextField(
            label = "New Password",
            value = newPassword.value,
            onValueChange = { newPassword.value = it },
            dmSansFont = dmSansFont
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password TextField
        PasswordTextField(
            label = "Confirm New Password",
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            dmSansFont = dmSansFont
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Validation Messages
        Text(
            text = "*Must contain at least 1 uppercase letter, 1 number, and 1 symbol\n*Password must contain at least 8 characters",
            fontFamily = dmSansFont,
            fontSize = 12.sp,
            color = Color.Red,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Error Message
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                fontFamily = dmSansFont,
                fontSize = 14.sp,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Confirm Button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(55.dp)
                .background(Color(0xFF43766C), RoundedCornerShape(16.dp))
                .clickable {
                    if (newPassword.value != confirmPassword.value) {
                        errorMessage.value = "New Password and Confirm Password do not match"
                    } else if (!isPasswordValid(newPassword.value)) {
                        errorMessage.value = "Password does not meet requirements"
                    } else {
                        errorMessage.value = ""
                        coroutineScope.launch {
                            profileViewModel.changePassword(
                                currentPassword = oldPassword.value,
                                newPassword = newPassword.value,
                                onSuccess = {
                                    errorMessage.value = "Password updated successfully"
                                    navController.popBackStack()
                                },
                                onError = { error ->
                                    errorMessage.value = error
                                }
                            )
                        }
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

@Composable
fun PasswordTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    dmSansFont: FontFamily
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label, fontFamily = dmSansFont) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFDF6E3), RoundedCornerShape(8.dp)),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = painterResource(
                        id = if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    ),
                    contentDescription = null
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
}

fun isPasswordValid(password: String): Boolean {
    val regex = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*]).{8,}$")
    return regex.matches(password)
}

