package com.example.android.appweather.retrofit

import com.example.android.appweather.model.WeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getWeeklyForecast(
        @Query("q") location: String,
        @Query("days") numberOfDays: Int,
        @Query("key") apiKey: String,
        @Query("lang") languageCode: String = "ru"
    ): Response<WeatherForecast>
}
