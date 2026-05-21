package com.android.agriguide.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.agriguide.R

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