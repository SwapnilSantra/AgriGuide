package com.android.agriguide.ui.theme
import retrofit2.Retrofit
import android.content.Context
import retrofit2.converter.gson.GsonConverterFactory

object Retrofithelper {
    const val WEATHER_URL = "https://api.open-meteo.com/"
    fun <S> createRetrofitService(context:Context, serviceClass:Class<S>):S{
        return buildRetrofit(context).create(serviceClass)
    }

    fun buildRetrofit(context:Context):Retrofit{
        return Retrofit.Builder().baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}