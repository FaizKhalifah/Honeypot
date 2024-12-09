// EditProductScreen.kt
package com.praktikum.honeypot.Screen.Product

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun EditProductScreen(
    product: Product, // Product to edit
    viewModel: ProductViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf(product.name) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price_per_unit.toString()) }
    var stock by remember { mutableStateOf(product.stock.toString()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(product.image_url?.let { Uri.parse(it) }) }
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
            // Image Preview with 1:1 Aspect Ratio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Maintain 1:1 aspect ratio
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .clickable { onTakePhotoClick() },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder_image),
                        contentDescription = "Placeholder Image",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Overlay camera icon
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera Icon",
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(8.dp)
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
                value = price,
                onValueChange = { price = it },
                label = { Text("Price Per Unit") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    if (name.isBlank() || description.isBlank() || stock.isBlank() || price.isBlank()) {
                        Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val updatedProduct = product.copy(
                        name = name,
                        description = description,
                        price_per_unit = price.toIntOrNull() ?: 0,
                        stock = stock.toIntOrNull() ?: 0
                    )

                    viewModel.updateProduct(updatedProduct, selectedImageFile,
                        onSuccess = {
                            Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }

        // Show Camera Preview as an Overlay within a Square Area
        if (showCamera) {
            // Semi-transparent background to focus on the camera preview
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable { /* Do nothing to prevent click-through */ }
            )

            // Centered Square Camera Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 80% of the screen width
                    .aspectRatio(1f) // 1:1 aspect ratio
                    .align(Alignment.Center)
                    .background(Color.Black)
            ) {
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
                )
            }
        }
    }
}
