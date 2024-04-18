package com.example.android.appweather.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.appweather.model.WeatherForecast
import com.example.android.appweather.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

import androidx.lifecycle.*
import com.example.android.appweather.model.Location
import com.example.android.appweather.model.TimeOfDay
import com.example.android.appweather.model.determineTimeOfDay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZoneId


class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    // изменяемое состояние для хранения текущей погоды
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    // StateFlow для наблюдения за изменениями
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    init {
        fetchWeatherData("Караганда") // город по умолчанию
    }

    // асинхронный запрос данных о погоде
    fun fetchWeatherData(location: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                // получаем данные о погоде из репозитория
                val result = repository.getWeeklyForecast(location)
                result?.let {
                    // если все ок - обновляем состояние
                    _weatherState.value = WeatherState.Success(it)
                } ?: run {
                    _weatherState.value = WeatherState.Error("No weather data found.")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
                _weatherState.value = WeatherState.Error(e.message ?: "Unknown error")
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun currentTimeOfDay(location: Location): TimeOfDay {
        val zoneId = ZoneId.of(location.tz_id)
        val currentLocalTime = LocalTime.now(zoneId)
        return determineTimeOfDay(currentLocalTime)
    }
}

// Фабрика для создания WeatherViewModel с зависимостями
class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Состояние погоды для UI
sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherForecast) : WeatherState()
    data class Error(val message: String) : WeatherState()
}
