package com.github.slavikjunior.weather_by_city.internal.viewmodels.events

internal sealed interface WeatherByCityEvent {

    data class GetCurrentWeatherEvent(val city: String): WeatherByCityEvent
}