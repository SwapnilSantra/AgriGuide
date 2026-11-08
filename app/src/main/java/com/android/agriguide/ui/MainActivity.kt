package com.android.agriguide.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.agriguide.R
import com.android.agriguide.Retrofithelper
import com.android.agriguide.Secrets
import com.android.agriguide.apiservice
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            val service = Retrofithelper.createRetrofitService(this@MainActivity, apiservice::class.java)
            val weatherkey = Secrets.WEATHER_API
            lifecycleScope.launch {
                try {
                    val response = service.getWeather(
                        Retrofithelper.WEATHER_URL + weatherkey
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
                            tvSoilStatus.text = "@string/NOTA"
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
                                tvHumidityStatus.text = "@string/NOTA"
                            }


                        tvLastUpdated.text = "@string/LST ${hr?.time?.getOrNull(idx) ?: "@string/Null"}"
                        tvOverallStatus.text = "@string/STGOOD"

                        tvTempValue.text = temp?.let { "$it ${units?.get("temperature_2m") ?: "@string/Celsius"}" } ?: "@string/NOTA"
                        tvHumidityValue.text = hum?.let { "${it.toInt()} %" } ?: "@string/NOTA"
                        tvSoilValue.text = soil?.let { "%.2f @string/soilval".format(it) } ?: "@string/NOTA"
                        tvLightValue.text = light?.let { "${it.toInt()} @string/Lightintensity" } ?: "@string/NOTA"
                    } else {
                        tvTempValue.text = "@string/ERR"
                        tvSoilValue.text = "@string/ERR"
                        tvHumidityValue.text = "@string/ERR"
                        tvLightValue.text = "@string/ERR"
                    }
                } catch (e: Exception) {
                    tvTempValue.text = "@string/FAIL"
                    tvSoilValue.text = "@string/FAIL"
                    tvHumidityValue.text = "@string/FAIL"
                    tvLightValue.text = "@string/FAIL"
                }
                finally {
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    private fun currentHourIndex(times: List<String>?): Int {
        if (times.isNullOrEmpty()) return 0
        val now = LocalDateTime.now()
            .withMinute(0).withSecond(0).withNano(0)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        val i = times.indexOf(now)
        return if (i >= 0) i else 0
    }
    }