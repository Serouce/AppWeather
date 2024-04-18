package com.example.android.appweather


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import java.time.format.DateTimeFormatter

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest

import com.example.android.appweather.model.CurrentWeather
import com.example.android.appweather.model.ForecastDay
import com.example.android.appweather.model.ImageLoader
import com.example.android.appweather.model.TimeOfDay
import com.example.android.appweather.model.WeatherForecast
import com.example.android.appweather.model.backgroundGradientBasedOnTime


import com.example.android.appweather.model.determineTimeOfDay
import com.example.android.appweather.network.RetrofitInstance
import com.example.android.appweather.repository.WeatherRepository
import com.example.android.appweather.ui.theme.AppWeatherTheme
import com.example.android.appweather.viewmodel.WeatherState
import com.example.android.appweather.viewmodel.WeatherViewModel
import com.example.android.appweather.viewmodel.WeatherViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitInstance.api
        val repository = WeatherRepository(apiService)
        val factory = WeatherViewModelFactory(repository)

        val viewModelProvider = ViewModelProvider(this, factory)
        val viewModel = viewModelProvider.get(WeatherViewModel::class.java)

        setContent {
            AppWeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    WeatherScreen(viewModel)


                }
            }
        }
    }
}



/*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyWeatherComponent(forecastDays: List<ForecastDay>, zoneId: String) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme

    // Функция для форматирования даты в день недели
    fun formatDayOfWeek(dateString: String): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, dateFormatter)
        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        forecastDays.forEach { forecastDay ->
            val dayOfWeek = formatDayOfWeek(forecastDay.date)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .background(color = colors.surfaceVariant)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dayOfWeek,
                    style = typography.titleMedium,
                    color = colors.onSecondaryContainer
                )
                ImageLoader(
                    imageUrl = forecastDay.day.condition.icon,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(R.string.weather_condition_image)
                )
                Row {
                    Text(
                        text = "${forecastDay.day.maxtemp_c}°",
                        style = typography.bodyLarge,
                        color = colors.onSurface
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "${forecastDay.day.mintemp_c}°",
                        style = typography.bodyLarge,
                        color = colors.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}













@Composable
fun CurrentWeatherComponent(weatherForecast: WeatherForecast) {
    val typography = MaterialTheme.typography

    val animatedAlpha = remember { Animatable(0f) }

    // Анимация появления
    LaunchedEffect(key1 = true) {
        animatedAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weatherForecast.location.name,
            style = typography.headlineMedium
        )
        Text(
            text = weatherForecast.current.condition.text,
            style = typography.titleMedium
        )
        ImageLoader(
            imageUrl = "https:${weatherForecast.current.condition.icon}",
            modifier = Modifier
                .size(88.dp)
                .alpha(animatedAlpha.value),
            contentDescription = "Weather icon"
        )
        Text(
            text = "${weatherForecast.current.temp_c}°C",
            style = typography.displayMedium
        )
        // Дополнительная информация о погоде может быть здесь, если нужно
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()
    val colors = MaterialTheme.colorScheme

    when (weatherState) {
        is WeatherState.Loading -> LoadingScreen()
        is WeatherState.Success -> {
            val weatherData = (weatherState as WeatherState.Success).data
            val currentTimeOfDay = viewModel.currentTimeOfDay(weatherData.location)

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = backgroundColorBasedOnTime(currentTimeOfDay)
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    CurrentWeatherComponent(weatherData)
                    Divider(color = colors.onSurface.copy(alpha = 0.2f))
                    WeeklyWeatherComponent(weatherData.forecast.forecastday, weatherData.location.tz_id)
                }
            }
        }
        is WeatherState.Error -> ErrorScreen((weatherState as WeatherState.Error).message)
    }
}


@Composable
fun ErrorScreen(errorMessage: String) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Центрируем сообщение об ошибке на экране
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(
            text = errorMessage,
            color = colors.error,
            style = typography.bodyLarge
        )
    }
}

@Composable
fun LoadingScreen() {
    val colors = MaterialTheme.colorScheme

        // центрируем
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(color = colors.primary)
    }
}


*/


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun WeeklyWeatherComponent(forecastDays: List<ForecastDay>, zoneId: String) {
//    val typography = MaterialTheme.typography
//    val colors = MaterialTheme.colorScheme
//
//    // Функция для форматирования даты в день недели с заглавной буквы
//    fun formatDayOfWeek(dateString: String): String {
//        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val date = LocalDate.parse(dateString, dateFormatter)
//        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).replaceFirstChar { it.uppercase() }
//    }
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        forecastDays.forEach { forecastDay ->
//            val dayOfWeek = formatDayOfWeek(forecastDay.date)
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp, horizontal = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = dayOfWeek,
//                    style = typography.titleMedium,
//                    modifier = Modifier.weight(1f)
//
//                )
//                ImageLoader(
//                    imageUrl = "https:${forecastDay.day.condition.icon}",
//                    modifier = Modifier
//                        .size(50.dp),
//                    contentDescription = "Weather condition icon"
//                )
//                Text(
//                    text = "${forecastDay.day.maxtemp_c.roundToInt()}°",
//                    style = typography.bodyLarge,
//                    color = colors.onSurface
//                )
//                Text(
//                    text = "${forecastDay.day.mintemp_c.roundToInt()}°",
//                    style = typography.bodyLarge,
//                    color = colors.onSurfaceVariant
//                )
//            }
//        }
//    }
//}
//
//
//

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyWeatherComponent(forecastDays: List<ForecastDay>, zoneId: String) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme
    val iconSize = 48.dp // Установите желаемый размер для иконок
    val temperatureWidth = 48.dp // Минимальная ширина для столбца температуры

    // Функция для форматирования даты в день недели
    fun formatDayOfWeek(dateString: String): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, dateFormatter)
        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).replaceFirstChar { it.uppercase() }
    }

    Column {
        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), thickness = 1.dp)
        forecastDays.forEachIndexed { index, forecastDay ->

            val dayOfWeek = formatDayOfWeek(forecastDay.date)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dayOfWeek,
                    style = typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                ImageLoader(
                    imageUrl = "https:${forecastDay.day.condition.icon}",
                    modifier = Modifier
                        .size(iconSize)
                        .weight(0.2f),
                    contentDescription = "Weather condition icon"
                )
                Spacer(modifier = Modifier.width(8.dp)) // Добавим немного пространства между иконкой и температурой
                Text(
                    text = "${forecastDay.day.maxtemp_c.roundToInt()}°",
                    style = typography.bodyLarge,
                    modifier = Modifier
                        .width(temperatureWidth)
                        .weight(0.3f),
                    textAlign = TextAlign.End // Выравниваем текст по правому краю
                )
                Text(
                    text = "${forecastDay.day.mintemp_c.roundToInt()}°",
                    style = typography.bodyLarge,
                    modifier = Modifier
                        .weight(0.3f),
                    textAlign = TextAlign.End // Выравниваем текст по правому краю
                )
            }
            if (index < forecastDays.size - 1) {
                Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), thickness = 1.dp)
            }
        }
        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), thickness = 1.dp)

    }
}













@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentWeatherComponent(weatherForecast: WeatherForecast) {
    val typography = MaterialTheme.typography

    val zoneId = ZoneId.of(weatherForecast.location.tz_id)
    val currentDateTime = LocalDateTime.now(zoneId) // Получаем текущие дату и время
    val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("EEE, MMM d")) // Форматируем дату

    val animatedAlpha = remember { Animatable(0f) }

    // Анимация появления
    LaunchedEffect(key1 = true) {
        animatedAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weatherForecast.location.name,
            style = typography.headlineLarge
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = weatherForecast.current.condition.text,
            style = typography.bodyLarge
        )
        ImageLoader(
            imageUrl = "https:${weatherForecast.current.condition.icon}",
            modifier = Modifier
                .size(150.dp)
                .alpha(animatedAlpha.value),
            contentDescription = "Weather icon"
        )
        Text(
            text = "${weatherForecast.current.temp_c.roundToInt()}°C",
            style = typography.displayMedium
        )
        Text(
            text = formattedDate,
            style = typography.titleMedium
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()

    when (weatherState) {
        is WeatherState.Loading -> LoadingScreen()
        is WeatherState.Success -> {
            val weatherData = (weatherState as WeatherState.Success).data
            val currentTimeOfDay = viewModel.currentTimeOfDay(weatherData.location)

            // Получаем градиент на основе времени суток
            val backgroundBrush = backgroundGradientBasedOnTime(currentTimeOfDay)

            // Используем градиент в качестве фона
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundBrush)
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    CurrentWeatherComponent(weatherData)
                    Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                    WeeklyWeatherComponent(weatherData.forecast.forecastday, weatherData.location.tz_id)
                }
            }
        }
        is WeatherState.Error -> ErrorScreen((weatherState as WeatherState.Error).message)
    }
}




@Composable
fun ErrorScreen(errorMessage: String) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Центрируем сообщение об ошибке на экране
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(
            text = errorMessage,
            color = colors.error,
            style = typography.bodyLarge
        )
    }
}

@Composable
fun LoadingScreen() {
    val colors = MaterialTheme.colorScheme

    // центрируем
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(color = colors.primary)
    }
}
