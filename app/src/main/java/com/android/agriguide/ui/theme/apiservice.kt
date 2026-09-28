package com.android.agriguide.ui.theme

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url


interface apiservice {
    @GET
    suspend fun getWeather(@Url url:String):retrofit2.Response<weatherapi>
}