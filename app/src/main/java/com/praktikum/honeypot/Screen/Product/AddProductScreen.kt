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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.*
import com.praktikum.honeypot.Data.Product
import com.praktikum.honeypot.ViewModel.ProductViewModel
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Camera.CameraPreview
import com.praktikum.honeypot.Util.BitmapUtils
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
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

    // Permission State for CAMERA
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    // Launcher for gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                val file = BitmapUtils.uriToFile(context, it)
                selectedImageFile = file
            }
        }
    )

    // Function to handle camera button click
    fun onTakePhotoClick() {
        when {
            cameraPermissionState.status.isGranted -> {
                // Permission is already granted, show the embedded camera
                showCamera = true
            }
            cameraPermissionState.status.shouldShowRationale -> {
                // Show rationale and request permission
                coroutineScope.launch {
                    Toast.makeText(context, "Camera permission is needed to take photos.", Toast.LENGTH_LONG).show()
                    cameraPermissionState.launchPermissionRequest()
                }
            }
            else -> {
                // Directly request permission
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Image Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { onTakePhotoClick() },
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.sugarcane),
                        contentDescription = "Placeholder Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Overlay camera icon
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera Icon",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons to choose image
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Take Photo Button
                Button(onClick = { onTakePhotoClick() }) {
                    Text("Take Photo")
                }

                // Select from Gallery Button
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Select from Gallery")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Fields
            OutlinedTextField(
                value = productId,
                onValueChange = { productId = it },
                label = { Text("Product Id") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pricePerUnit,
                onValueChange = { pricePerUnit = it },
                label = { Text("Price Per Unit") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Add Product Button
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
                        image_url = null // Image URL will be set by the server
                    )

                    viewModel.addProduct(newProduct, selectedImageFile,
                        onSuccess = {
                            Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product")
            }
        }

        // Show Camera Preview as a full-screen overlay
        if (showCamera) {
            CameraPreview(
                onImageCaptured = { file ->
                    selectedImageFile = file
                    selectedImageUri = Uri.fromFile(file)
                    showCamera = false
                },
                onError = { exc ->
                    Toast.makeText(context, "Image capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                    showCamera = false
                },
                onClose = { showCamera = false },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)) // Optional: Dim the background
            )
        }
    }
}
