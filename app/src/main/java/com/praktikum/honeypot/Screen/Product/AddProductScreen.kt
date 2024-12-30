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
    var productId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var pricePerUnit by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

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
            cameraPermissionState.status.isGranted -> showCamera = true
            cameraPermissionState.status.shouldShowRationale -> {
                coroutineScope.launch {
                    Toast.makeText(context, "Camera permission is needed to take photos.", Toast.LENGTH_LONG).show()
                    cameraPermissionState.launchPermissionRequest()
                }
            }
            else -> cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Add Product",
                        style = TextStyle(
                            fontFamily = dmSansFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            // Image Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAE5))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onTakePhotoClick() }
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
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
                            Icon(
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = "Add Photo",
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFF43766C)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Add Product Photo",
                                style = TextStyle(
                                    fontFamily = dmSansFontFamily,
                                    color = Color(0xFF43766C)
                                )
                            )
                        }
                    }
                }
            }

            // Image Source Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onTakePhotoClick() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF43766C)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Camera",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Camera")
                }
                
                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF43766C)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.gallery),
                        contentDescription = "Gallery",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gallery")
                }
            }

            // Form Fields
            OutlinedTextField(
                value = productId,
                onValueChange = { productId = it },
                label = { Text("Product ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF43766C),
                    focusedLabelColor = Color(0xFF43766C)
                )
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF43766C),
                    focusedLabelColor = Color(0xFF43766C)
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF43766C),
                    focusedLabelColor = Color(0xFF43766C)
                )
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF43766C),
                    focusedLabelColor = Color(0xFF43766C)
                )
            )

            OutlinedTextField(
                value = pricePerUnit,
                onValueChange = { pricePerUnit = it },
                label = { Text("Price Per Unit") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF43766C),
                    focusedLabelColor = Color(0xFF43766C)
                )
            )

            // Add Button
            Button(
                onClick = {
                    if (name.isBlank() || description.isBlank() || stock.isBlank() || pricePerUnit.isBlank()) {
                        Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newProduct = Product(
                        product_id = productId.toIntOrNull() ?: 0,
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
                            Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF43766C)
                )
            ) {
                Text(
                    "Add Product",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = dmSansFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Camera Preview Overlay
        if (showCamera) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                CameraPreview(
                    onImageCaptured = { file ->
                        selectedImageFile = file
                        selectedImageUri = Uri.fromFile(file)
                        showCamera = false
                    },
                    onError = { exc ->
                        Toast.makeText(context, "Failed to capture image: ${exc.message}", Toast.LENGTH_SHORT).show()
                        showCamera = false
                    },
                    onClose = { showCamera = false }
                )
            }
        }
    }
}
