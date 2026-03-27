package com.github.slavikjunior.weatherapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue80,
    secondary = SkyBlueGrey80,
    tertiary = SkyAccent80
)

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue40,
    secondary = SkyBlueGrey40,
    tertiary = SkyAccent40
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
