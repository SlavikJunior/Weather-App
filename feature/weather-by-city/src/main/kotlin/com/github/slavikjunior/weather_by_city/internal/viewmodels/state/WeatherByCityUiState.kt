package com.github.slavikjunior.weather_by_city.internal.viewmodels.state

internal sealed interface WeatherByCityUiState {

    data object LoadingState : WeatherByCityUiState
    data class ErrorState(val cause: Throwable) : WeatherByCityUiState
    data class DefaultState(

    ) : WeatherByCityUiState
}