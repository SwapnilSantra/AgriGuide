package com.android.agriguide.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.agriguide.R
import kotlinx.coroutines.launch
import kotlin.text.toInt

class MainActivity: AppCompatActivity() {
    private lateinit var swipeRefresh: SwipeRefreshLayout
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
        swipeRefresh = findViewById(R.id.swipeRefresh)
        buttondiseasedetection.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraViewActivity::class.java)
            startActivity(intent)
        }
        swipeRefresh.setOnRefreshListener {
            getweatherdata()
        }
    }
        private fun getweatherdata(){
            val service = Retrofithelper.createRetrofitService(this@MainActivity,apiservice::class.java)
            lifecycleScope.launch {
                try {
                    val response = service.getWeather(
                        Retrofithelper.WEATHER_URL + "v1/forecast?latitude=22.5626&longitude=88.363&hourly=temperature_2m,soil_moisture_9_to_27cm,relative_humidity_2m,shortwave_radiation&models=best_match&timezone=auto"
                    )
                    if (response.isSuccessful) {
                        val hr = response.body()?.hourly
                        val body = response.body()
                        //val hr = body?.hourly
                        val units = body?.hourlyUnits

                        val idx = currentHourIndex(hr?.time)

                        val temp = hr?.temperature?.getOrNull(idx)
                        val hum = hr?.humidity?.getOrNull(idx)
                        val soil = hr?.soilMoisture?.getOrNull(idx)
                        val light = hr?.sunlight?.getOrNull(idx)

                        soil?.let { s ->
                            tvSoilStatus.text = when {
                                s < 0.20 -> "Drought"
                                s < 0.35 -> "Dry / Warning"
                                s < 0.75 -> "Optimum"
                                s < 0.80 -> "Wet / Warning"
                                else -> "Saturated"
                            }
                        } ?: run {
                            tvSoilStatus.text = "N/A"
                        }
                        hum?.let { h ->
                            tvHumidityStatus.text = when {
                                h < 40 -> "very Dry"
                                h > 40 && h < 55 -> "Dry"
                                h > 55 && h < 80 -> "Optimum"
                                h > 80 && h <= 90 -> "Humid"
                                h > 90 -> "Very Humid"
                                else -> "Humidity not calculated"
                            }
                        }?: run {
                                tvHumidityStatus.text = "N/A"
                            }


                        tvLastUpdated.text = "Last updated: ${hr?.time?.getOrNull(idx) ?: "--"}"
                        tvOverallStatus.text = "Overall status: Good"

                        tvTempValue.text = temp?.let { "$it ${units?.get("temperature_2m") ?: "°C"}" } ?: "N/A"
                        tvHumidityValue.text = hum?.let { "${it.toInt()} %" } ?: "N/A"
                        tvSoilValue.text = soil?.let { "%.2f m³/m³".format(it) } ?: "N/A"
                        tvLightValue.text = light?.let { "${it.toInt()} W/m²" } ?: "N/A"
                    } else {
                        tvTempValue.text = "Error"
                        tvSoilValue.text = "Error"
                        tvHumidityValue.text = "Error"
                        tvLightValue.text = "Error"
                    }
                } catch (e: Exception) {
                    tvTempValue.text = "Failed"
                    tvSoilValue.text = "Failed"
                    tvHumidityValue.text = "Failed"
                    tvLightValue.text = "Failed"
                }
                finally {
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    private fun currentHourIndex(times: List<String>?): Int {
        if (times.isNullOrEmpty()) return 0
        val now = java.time.LocalDateTime.now()
            .withMinute(0).withSecond(0).withNano(0)
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        val i = times.indexOf(now)
        return if (i >= 0) i else 0
    }
    }
