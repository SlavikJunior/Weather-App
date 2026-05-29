package com.github.slavikjunior.weatherapp.presentation.screen.weatherDetail

internal sealed interface WeatherDetailEvent {
    data object RetryEvent : WeatherDetailEvent
    data object DismissErrorEvent : WeatherDetailEvent
}