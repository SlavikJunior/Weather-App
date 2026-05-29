package com.github.slavikjunior.weatherapp.presentation.screen.weatherByCity

internal sealed interface WeatherByCityEvent {

    data object GetCurrentWeatherEvent : WeatherByCityEvent
    data object DismissErrorEvent : WeatherByCityEvent
    data class UpdateCurrentCityEvent(val city: String) : WeatherByCityEvent
}