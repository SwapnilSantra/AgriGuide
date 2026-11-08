package com.android.agriguide.ui

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.agriguide.R

class ImageViewerActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imageviewer)

        imageView = findViewById(R.id.imageViewer)

        val uriString = intent.getStringExtra("CAPTURED_IMAGE_URI")
        if (uriString != null) {
            val myImageUri = Uri.parse(uriString)
            imageView.setImageURI(myImageUri)
        }
    }
}