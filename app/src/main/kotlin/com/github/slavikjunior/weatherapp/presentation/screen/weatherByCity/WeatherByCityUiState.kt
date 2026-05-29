package com.github.slavikjunior.weatherapp.presentation.screen.weatherByCity

import androidx.compose.runtime.Immutable
import com.github.slavikjunior.weatherapp.domain.model.WeatherData

@Immutable
internal sealed interface WeatherByCityUiState {

    @Immutable
    data class LoadingState(val requestedCity: String) : WeatherByCityUiState

    @Immutable
    data class ErrorState(
        val message: String,
        val city: String = ""
    ) : WeatherByCityUiState

    @Immutable
    data class DefaultState(
        val city: String = "",
        val weatherData: WeatherData? = null
    ) : WeatherByCityUiState
}