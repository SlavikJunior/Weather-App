package com.github.slavikjunior.weatherapp.presentation.screen.weatherDetail

import com.github.slavikjunior.weatherapp.domain.model.WeatherData

internal sealed interface WeatherDetailUiState {

    data class LoadingState(val requestedCity: String): WeatherDetailUiState

    data class ErrorState(
        val cause: Throwable,
        val city: String = ""
    ) : WeatherDetailUiState

    data class DefaultState(
        val city: String = "",
        val weatherData: WeatherData? = null
    ) : WeatherDetailUiState
}