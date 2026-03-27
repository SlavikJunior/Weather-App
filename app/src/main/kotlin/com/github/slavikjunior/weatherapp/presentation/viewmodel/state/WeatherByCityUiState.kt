package com.github.slavikjunior.weatherapp.presentation.viewmodel.state

import com.github.slavikjunior.weatherapp.domain.model.WeatherData

internal sealed interface WeatherByCityUiState {

    data class LoadingState(val requestedCity: String) : WeatherByCityUiState

    data class ErrorState(
        val cause: Throwable,
        val city: String = ""
    ) : WeatherByCityUiState

    data class DefaultState(
        val city: String = "",
        val weatherData: WeatherData? = null
    ) : WeatherByCityUiState
}
