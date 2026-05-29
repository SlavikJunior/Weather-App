package com.github.slavikjunior.weatherapp.presentation.screen.weatherDetail

import androidx.compose.runtime.Immutable
import com.github.slavikjunior.weatherapp.domain.model.WeatherData

@Immutable
internal sealed interface WeatherDetailUiState {

    @Immutable
    data class LoadingState(val requestedCity: String) : WeatherDetailUiState

    @Immutable
    data class ErrorState(
        val message: String,
        val city: String = ""
    ) : WeatherDetailUiState

    @Immutable
    data class DefaultState(
        val city: String = "",
        val weatherData: WeatherData? = null
    ) : WeatherDetailUiState
}