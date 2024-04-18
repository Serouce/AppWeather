package com.example.android.appweather.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.android.appweather.R
import java.time.LocalTime
import java.time.ZoneId

enum class TimeOfDay {
    MORNING, DAY, EVENING, NIGHT
}

@RequiresApi(Build.VERSION_CODES.O)
fun determineTimeOfDay(localTime: LocalTime): TimeOfDay = when (localTime.hour) {
    in 6..11 -> TimeOfDay.MORNING
    in 12..17 -> TimeOfDay.DAY
    in 18..22 -> TimeOfDay.EVENING
    else -> TimeOfDay.NIGHT
}

//fun backgroundColorBasedOnTime(timeOfDay: TimeOfDay): Color = when (timeOfDay) {
//    TimeOfDay.MORNING -> Color(0xFFFFD54F)
//    TimeOfDay.DAY -> Color(0xFF90CAF9)
//    TimeOfDay.EVENING -> Color(0xFF9575CD)
//    TimeOfDay.NIGHT -> Color(0xFF000080)
//}

//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun backgroundDrawableBasedOnTime(timeOfDay: TimeOfDay): Painter {
//    val context = LocalContext.current
//    val drawableId = when (timeOfDay) {
//        TimeOfDay.MORNING -> R.drawable.morning_ic
//        TimeOfDay.DAY -> R.drawable.morning_ic
//        TimeOfDay.EVENING -> R.drawable.morning_ic
//        TimeOfDay.NIGHT -> R.drawable.morning_ic
//    }
//    return painterResource(id = drawableId)
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun WeatherBackground(timeOfDay: TimeOfDay) {
//    val backgroundPainter = backgroundDrawableBasedOnTime(timeOfDay)
//    Image(
//        painter = backgroundPainter,
//        contentDescription = null, // Предоставьте описание, если это необходимо для доступности
//        modifier = Modifier.fillMaxSize(), // Или любой другой модификатор, который вам нужен
//        contentScale = ContentScale.Crop // Устанавливает, как изображение должно масштабироваться или обрезаться для заполнения пространства, если размеры изображения не соответствуют размерам модификатора
//    )
//}



@Composable
fun backgroundGradientBasedOnTime(timeOfDay: TimeOfDay): Brush {
    return when (timeOfDay) {
        TimeOfDay.MORNING -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFF8C00), Color(0xFFFFD700))
        )
        TimeOfDay.DAY -> Brush.verticalGradient(
            colors = listOf(Color(0xFF87CEEB), Color(0xFF00BFFF))
        )
        TimeOfDay.EVENING -> Brush.verticalGradient(
            colors = listOf(Color(0xFFDDA0DD), Color(0xFF6A5ACD))
        )
        TimeOfDay.NIGHT -> Brush.verticalGradient(
            colors = listOf(Color(0xFF00008B), Color(0xFF0000CD))
        )
    }
}
