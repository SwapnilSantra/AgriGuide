package com.android.agriguide.ui.theme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.android.agriguide.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class CameraViewActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null

    // Handles user permission choice, safely initiating camera stream on access approval
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraStream()
            } else {
                Toast.makeText(this, "Camera access is required for live view", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cameraview)

        // Maps cleanly to your XML layout definition
        previewView = findViewById(R.id.previewView)

        // Evaluate baseline permissions before spinning up the hardware stream
        checkPermissionsAndStart()

        findViewById<FloatingActionButton>(R.id.btnCapture).setOnClickListener {
            capturePhotoFromStream()
        }
    }

    private fun checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCameraStream()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCameraStream() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                // Instantiates structural lifecycle bindings link
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Instantiates video streaming layout engine adapter
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // Configures flash capture snapshot profiles
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // Disconnect existing references before binding new streaming pipelines
                cameraProvider.unbindAll()

                // Bind pipeline directly to Activity Lifecycle
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (e: Exception) {
                Log.e("CameraX", "Stream setup failure", e)
                Toast.makeText(this, "Failed to connect to system camera engine", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun capturePhotoFromStream() {
        val imageCapture = imageCapture ?: return

        // Builds a storage file container inside your safe internal application storage cache
        val outputPhotoFile = File(externalCacheDir, "agriguide_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputPhotoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val finalUri = Uri.fromFile(outputPhotoFile)
                    Toast.makeText(baseContext, "Photo saved!", Toast.LENGTH_SHORT).show()
                    Log.d("CameraX", "Captured media successfully stored at: $finalUri")

                    // 1. Fire up the ImageViewer screen
                    val intent = Intent(this@CameraViewActivity, ImageViewerActivity::class.java).apply {
                        putExtra("CAPTURED_IMAGE_URI", finalUri.toString())
                    }
                    startActivity(intent)

                    // 2. Process local file data
                    val imageFile = outputPhotoFile
                    if (imageFile.exists()) {
                        Log.d("CameraX", "File Size: ${imageFile.length()} bytes")
                        Log.d("CameraX", "Absolute Path: ${imageFile.absolutePath}")

                        val bitmap = android.graphics.BitmapFactory.decodeFile(imageFile.absolutePath)
                        // You can process your bitmap data right here if needed
                    } // Fixed: Added missing closing brace for imageFile.exists()
                }

                // Fixed: Added the critical 'override' keyword here
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Snapshot execution error: ${exception.message}", exception)
                    Toast.makeText(baseContext, "Failed to complete photo capture", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}