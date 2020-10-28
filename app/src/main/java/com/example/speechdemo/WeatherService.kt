package com.example.speechdemo

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") q: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

}