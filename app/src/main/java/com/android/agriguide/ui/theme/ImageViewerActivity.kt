package com.android.agriguide.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.agriguide.R
import android.net.Uri
import android.widget.ImageView

class ImageViewerActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imageviewer)

        // Fixed: Assigned directly to the class property (removed the 'val' keyword)
        imageView = findViewById(R.id.imageViewer)

        val uriString = intent.getStringExtra("CAPTURED_IMAGE_URI")
        if (uriString != null) {
            val myImageUri = Uri.parse(uriString)
            imageView.setImageURI(myImageUri)
        } // Fixed: Added the missing closing brace for the 'if' condition
    }
}