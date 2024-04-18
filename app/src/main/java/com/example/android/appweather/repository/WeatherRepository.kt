package com.example.android.appweather.repository

import android.util.Log
import com.example.android.appweather.model.WeatherForecast
import com.example.android.appweather.network.RetrofitInstance
import com.example.android.appweather.retrofit.WeatherApiService

class WeatherRepository(private val apiService: WeatherApiService) {
    suspend fun getWeeklyForecast(location: String, lang: String = "ru"): WeatherForecast? {
        // ключ надо спрятать
        val response = apiService.getWeeklyForecast(location, 5, "e487a5feefeb42ec8b4144837241504")
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("WeatherRepository", "Не удалось получить данные о погоде: ${response.errorBody()?.string()}")
            null
        }
    }
}
