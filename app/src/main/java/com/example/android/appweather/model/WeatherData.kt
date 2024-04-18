package com.example.android.appweather.model



data class WeatherForecast(
    val location: Location,
    val current: CurrentWeather,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,  // ID часового пояса
    val localtime: String  // Локальное время
)

data class Condition(
    val text: String,  // Описание погоды
    val icon: String,  // URL иконки погоды
    val code: Int      // Код погодного состояния
)

data class CurrentWeather(
    val temp_c: Double,  // Температура в градусах
    val condition: Condition,
    val is_day: Int,
    val location: Location
// 1 если день, 0 если ночь
)

data class Forecast(
    val forecastday: List<ForecastDay>  // Список прогнозов по дням
)

data class ForecastDay(
    val date: String,
    val day: DayForecast,
    val astro: Astro,
    val hour: List<HourForecast>  // Прогноз по часам
)

data class DayForecast(
    val maxtemp_c: Double,  // Максимальная температура дня
    val mintemp_c: Double,  // Минимальная температура дня
    val avgtemp_c: Double,  // Средняя температура дня
    val maxwind_kph: Double,  // Максимальная скорость ветра в км/ч
    val totalprecip_mm: Double,  // Общее количество осадков в мм
    val avgvis_km: Double,  // Средняя видимость в км
    val avghumidity: Double,  // Средняя влажность в %
    val condition: Condition,
    val uv: Double  // Ультрафиолетовый индекс

)

data class Astro(
    val sunrise: String,  // Время восхода
    val sunset: String  // Время заката

)

data class HourForecast(
    val time: String,
    val temp_c: Double,
    val condition: Condition,
    val will_it_rain: Int,  // 1 если ожидаются дожди, 0 если нет
    val chance_of_rain: Int  // Шанс дождя в процентах
)
