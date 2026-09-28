package com.android.agriguide.ui.theme

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class weatherapi(
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("timezone") val timezone: String? = null,
    @SerializedName("hourly_units") val hourlyUnits: Map<String, String>? = null,
    @SerializedName("hourly") val hourly: Hourly? = null
) : Serializable

data class Hourly(
    @SerializedName("time") val time: List<String>? = null,
    @SerializedName("temperature_2m") val temperature: List<Double?>? = null,
    @SerializedName("relative_humidity_2m") val humidity: List<Double?>? = null,
    @SerializedName("soil_moisture_9_to_27cm") val soilMoisture: List<Double?>? = null,
    @SerializedName("shortwave_radiation") val sunlight: List<Double?>? = null
) : Serializable