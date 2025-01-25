package com.praktikum.honeypot.Screen.Auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Screen.Home.dmSansFontFamily
import com.praktikum.honeypot.ViewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val authViewModel = AuthViewModel(LocalContext.current)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-1).dp)
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

            // Middle section with main content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .offset(y = (-80).dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Welcome Text Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Create Account",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43766C)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sign up to get started",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    )
                }

                // Register Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Username Field
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { 
                                Text(
                                    "Username",
                                    style = TextStyle(
                                        fontFamily = dmSansFontFamily,
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF43766C),
                                unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f),
                                focusedLabelColor = Color(0xFF43766C),
                                cursorColor = Color(0xFF43766C)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.person),
                                    contentDescription = "Username",
                                    tint = Color(0xFF43766C),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )

                        // Password Field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { 
                                Text(
                                    "Password",
                                    style = TextStyle(
                                        fontFamily = dmSansFontFamily,
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF43766C),
                                unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f),
                                focusedLabelColor = Color(0xFF43766C),
                                cursorColor = Color(0xFF43766C)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.lock),
                                    contentDescription = "Password",
                                    tint = Color(0xFF43766C),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )

                        // Full Name Field
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { 
                                Text(
                                    "Nama Lengkap",
                                    style = TextStyle(
                                        fontFamily = dmSansFontFamily,
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF43766C),
                                unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f),
                                focusedLabelColor = Color(0xFF43766C),
                                cursorColor = Color(0xFF43766C)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.person),
                                    contentDescription = "Full Name",
                                    tint = Color(0xFF43766C),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )

                        // Contact Field
                        OutlinedTextField(
                            value = contact,
                            onValueChange = { contact = it },
                            label = { 
                                Text(
                                    "Kontak",
                                    style = TextStyle(
                                        fontFamily = dmSansFontFamily,
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF43766C),
                                unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f),
                                focusedLabelColor = Color(0xFF43766C),
                                cursorColor = Color(0xFF43766C)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.phone),
                                    contentDescription = "Contact",
                                    tint = Color(0xFF43766C),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )

                        // Error Message if exists
                        if (errorMessage.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.info),
                                        contentDescription = "Error",
                                        tint = Color(0xFFF57C00),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = errorMessage,
                                        style = TextStyle(
                                            fontFamily = dmSansFontFamily,
                                            fontSize = 12.sp,
                                            color = Color(0xFFF57C00)
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Register Button
                        Button(
                            onClick = {
                                isLoading = true
                                errorMessage = ""
                                authViewModel.register(
                                    username = username,
                                    password = password,
                                    full_name = fullName,
                                    contact = contact,
                                    onSuccess = {
                                        isLoading = false
                                        onNavigateToLogin()
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        errorMessage = error
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF43766C),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "Register",
                                    style = TextStyle(
                                        fontFamily = dmSansFontFamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                // Login Link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sudah punya akun? ",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    )
                    Text(
                        text = "Login di sini",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF43766C)
                        ),
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }

            // Bottom section with contact info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Jika App Bermasalah Tolong Hubungi Contact Kemarin",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}
