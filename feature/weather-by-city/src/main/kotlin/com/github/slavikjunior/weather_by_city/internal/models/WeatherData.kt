package com.github.slavikjunior.weather_by_city.internal.models

internal data class WeatherData(
    val name: String,
    val description: String,
    val icon: String,
    val temp: Int,
    val feelsLike: Int,
    val windSpeed: Int,
    val sunrise: Long,
    val sunSet: Long
)
