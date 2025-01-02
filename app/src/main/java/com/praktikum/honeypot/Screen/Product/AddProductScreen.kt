// AddProductScreen.kt
package com.praktikum.honeypot.Screen.Product

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Screen.Home.dmSansFontFamily
import com.praktikum.honeypot.Camera.CameraPreview
import com.praktikum.honeypot.Util.BitmapUtils
import com.praktikum.honeypot.ViewModel.ProductViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddProductScreen(
    viewModel: ProductViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var pricePerUnit by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                selectedImageFile = BitmapUtils.uriToFile(context, it)
            }
        }
    )

    fun onTakePhotoClick() {
        when {
            cameraPermissionState.status.isGranted -> {
                showCamera = true
            }
            cameraPermissionState.status.shouldShowRationale -> {
                coroutineScope.launch {
                    Toast.makeText(context, "Camera permission is needed to take photos.", Toast.LENGTH_LONG).show()
                    cameraPermissionState.launchPermissionRequest()
                }
            }
            else -> {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Logo and Back Button Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tambah Produk",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Image Preview Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable { onTakePhotoClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.camera),
                                    contentDescription = "Camera Icon",
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tambah Foto Produk",
                                    style = TextStyle(
                                        fontFamily = dmSansFontFamily,
                                        color = Color.Gray
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Image Source Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onTakePhotoClick() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43766C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Ambil Foto",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43766C)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Pilih Galeri",
                            style = TextStyle(
                                fontFamily = dmSansFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Input Fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Produk") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF43766C),
                        unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF43766C),
                        unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stok") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF43766C),
                        unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = pricePerUnit,
                    onValueChange = { pricePerUnit = it },
                    label = { Text("Harga per Unit") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF43766C),
                        unfocusedBorderColor = Color(0xFF43766C).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Add Product Button - Fixed at bottom
            Button(
                onClick = {
                    if (name.isBlank() || description.isBlank() || stock.isBlank() || pricePerUnit.isBlank()) {
                        Toast.makeText(context, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newProduct = Product(
                        product_id = 0, // This will be ignored by the API
                        name = name,
                        description = description,
                        stock = stock.toIntOrNull() ?: 0,
                        price_per_unit = pricePerUnit.toIntOrNull() ?: 0,
                        image_url = null
                    )

                    viewModel.addProduct(
                        newProduct,
                        selectedImageFile,
                        onSuccess = {
                            Toast.makeText(context, "Produk berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43766C)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Tambah Produk",
                    style = TextStyle(
                        fontFamily = dmSansFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // Camera Preview Overlay
        if (showCamera) {
            CameraPreview(
                onImageCaptured = { file ->
                    selectedImageFile = file
                    selectedImageUri = Uri.fromFile(file)
                    showCamera = false
                },
                onError = { exc ->
                    Toast.makeText(context, "Gagal mengambil foto: ${exc.message}", Toast.LENGTH_SHORT).show()
                    showCamera = false
                },
                onClose = { showCamera = false },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
            )
        }
    }
}
