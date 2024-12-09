// CameraPreview.kt
package com.praktikum.honeypot.Camera

import android.content.Context
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.praktikum.honeypot.R
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    onImageCaptured: (imageFile: File) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) { // Hapus fillMaxSize di sini
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        var previewView by remember { mutableStateOf<androidx.camera.view.PreviewView?>(null) }
        var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
        val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

        LaunchedEffect(previewView) {
            if (previewView != null) {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                val cameraProvider = cameraProviderFuture.get()

                // Ubah resolusi target ke 720x720 untuk aspek rasio 1:1
                val squareResolution = android.util.Size(720, 720)

                // Inisialisasi Preview
                val preview = Preview.Builder()
                    .setTargetResolution(squareResolution)
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView?.surfaceProvider)
                    }

                // Inisialisasi ImageCapture
                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(squareResolution)
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", exc)
                }
            }
        }

        AndroidView(
            factory = { ctx ->
                val view = androidx.camera.view.PreviewView(ctx)
                view.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                // Sesuaikan scale type untuk menjaga aspek rasio 1:1
                view.scaleType = androidx.camera.view.PreviewView.ScaleType.FIT_CENTER
                previewView = view
                view
            },
            modifier = Modifier
                .fillMaxSize() // Isi parent sesuai ukuran yang ditentukan
        )

        // Tombol Capture
        Button(
            onClick = {
                val imageCaptureUseCase = imageCapture ?: return@Button

                val photoFile = File(
                    context.cacheDir,
                    "IMG_${System.currentTimeMillis()}.jpg"
                )

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                imageCaptureUseCase.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            onImageCaptured(photoFile)
                        }

                        override fun onError(exc: ImageCaptureException) {
                            onError(exc)
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            Text("Capture")
        }

        // Tombol Close
        IconButton(
            onClick = { onClose() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Close Camera",
                tint = Color.White
            )
        }
    }
}
