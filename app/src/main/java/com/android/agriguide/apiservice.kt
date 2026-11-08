package com.android.agriguide

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface apiservice {
    @GET
    suspend fun getWeather(@Url url:String): Response<weatherapi>
}