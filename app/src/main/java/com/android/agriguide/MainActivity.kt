package com.android.agriguide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.agriguide.ui.theme.AgriGuideTheme
import com.android.agriguide.ui.theme.CameraViewActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity: AppCompatActivity() {
    private lateinit var tvFieldName: TextView
    private lateinit var tvOverallStatus: TextView
    private lateinit var tvLastUpdated: TextView

    private lateinit var tvSoilValue: TextView
    private lateinit var tvSoilStatus: TextView

    private lateinit var tvTempValue: TextView
    private lateinit var tvTempStatus: TextView

    private lateinit var tvHumidityValue: TextView
    private lateinit var tvHumidityStatus: TextView

    private lateinit var tvLightValue: TextView
    private lateinit var tvLightStatus: TextView
    private lateinit var buttondiseasedetection: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        tvFieldName = findViewById(R.id.tvFieldName)
        tvOverallStatus = findViewById(R.id.tvOverallStatus)
        tvLastUpdated = findViewById(R.id.tvLastUpdated)

        tvSoilValue = findViewById(R.id.tvSoilValue)
        tvSoilStatus = findViewById(R.id.tvSoilStatus)

        tvTempValue = findViewById(R.id.tvTempValue)
        tvTempStatus = findViewById(R.id.tvTempStatus)

        tvHumidityValue = findViewById(R.id.tvHumidityValue)
        tvHumidityStatus = findViewById(R.id.tvHumidityStatus)

        tvLightValue = findViewById(R.id.tvLightValue)
        tvLightStatus = findViewById(R.id.tvLightStatus)
        buttondiseasedetection = findViewById(R.id.btnDiseaseDetection)
        buttondiseasedetection.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraViewActivity::class.java)
            startActivity(intent)
        }
}
}