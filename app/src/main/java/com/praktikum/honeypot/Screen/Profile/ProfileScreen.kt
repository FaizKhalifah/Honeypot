package com.praktikum.honeypot.Screen.Profile

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.praktikum.honeypot.Factory.AppViewModelFactory
import com.praktikum.honeypot.R
import com.praktikum.honeypot.Util.PreferencesHelper
import com.praktikum.honeypot.ViewModel.AppStateViewModel
import com.praktikum.honeypot.ViewModel.ProfileViewModel
import com.praktikum.honeypot.Util.BitmapUtils
import com.praktikum.honeypot.Camera.CameraPreview
import java.io.File

@Composable
fun ImagePreviewDialog(
    imageUri: Uri,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Preview Image",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Image preview with zoom and pan
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Preview",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(0.5f, 3f)
                                offsetX += pan.x * scale
                                offsetY += pan.y * scale
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43766C))
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(navController: NavController, appStateViewModel: AppStateViewModel) {
    val context = LocalContext.current
    val preferencesHelper = PreferencesHelper(context)
    val profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelFactory(context))
    val profile = profileViewModel.profile.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    var showCamera by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var showImagePreview by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }

    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                selectedImageFile = BitmapUtils.uriToFile(context, it)
                showImagePreview = true
            }
        }
    )

    fun onTakePhotoClick() {
        when {
            cameraPermissionState.status.isGranted -> showCamera = true
            cameraPermissionState.status.shouldShowRationale -> {
                Toast.makeText(context, "Camera permission is needed to take photos.", Toast.LENGTH_LONG).show()
                cameraPermissionState.launchPermissionRequest()
            }
            else -> cameraPermissionState.launchPermissionRequest()
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Choose Image Source") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            onTakePhotoClick()
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.camera),
                                contentDescription = "Camera",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Take Photo")
                        }
                    }
                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            galleryLauncher.launch("image/*")
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.gallery),
                                contentDescription = "Gallery",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Choose from Gallery")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showImageSourceDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    val refreshState = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("refreshProfile")
        ?.observeAsState(initial = false)

    if (refreshState?.value == true) {
        profileViewModel.fetchProfile()
        navController.currentBackStackEntry?.savedStateHandle?.set("refreshProfile", false)
    }

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

        // Back Arrow and Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(
                text = "Profile",
                fontFamily = dmSansFont,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture with click handler
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray, CircleShape)
                .clickable { showImageSourceDialog = true }
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF43766C),
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else {
                AsyncImage(
                    model = profile.value?.profile_image_url ?: R.drawable.placeholder_image,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.placeholder_image)
                )
                // Camera icon overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Edit Photo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Owner's Name and Username
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF43766C))
            }
        } else {
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Details
        if (isLoading) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .background(Color(0xFF43766C), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
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
                ProfileDetailItem(
                    label = "Password",
                    value = "*********",
                    fontFamily = dmSansFont,
                    onClick = {
                        navController.navigate("editPasswordScreen")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(55.dp)
                .background(Color(0xFFE84949), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Logout",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = dmSansFont,
                modifier = Modifier.clickable {
                    // Logout logic
                    appStateViewModel.logOut()
                    profileViewModel.logout(
                        onSuccess = {
                            navController.navigate("login") {
                                popUpTo("main") { inclusive = true }
                            }
                        },
                        onError = { errorMessage ->
                            println("Logout Error: $errorMessage")
                        }
                    )
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp)) // Space above the navbar

    // Camera Preview Overlay
    if (showCamera) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(enabled = false) {}
        ) {
            CameraPreview(
                onImageCaptured = { file ->
                    selectedImageFile = file
                    selectedImageUri = Uri.fromFile(file)
                    showCamera = false
                    showImagePreview = true
                },
                onError = { exc ->
                    Toast.makeText(context, "Failed to capture image: ${exc.message}", Toast.LENGTH_SHORT).show()
                    showCamera = false
                },
                onClose = { showCamera = false }
            )
        }
    }

    if (showImagePreview && selectedImageUri != null) {
        ImagePreviewDialog(
            imageUri = selectedImageUri!!,
            onConfirm = {
                showImagePreview = false
                profileViewModel.updateProfileImage(
                    selectedImageFile!!,
                    onSuccess = {
                        Toast.makeText(context, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show()
                        profileViewModel.fetchProfile()
                    },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onDismiss = {
                showImagePreview = false
                selectedImageUri = null
                selectedImageFile = null
            }
        )
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
