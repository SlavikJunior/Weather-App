package com.github.slavikjunior.weatherapp.domain.model

internal data class WeatherData(
    val name: String,
    val description: String,
    val icon: String,
    val temp: Int,
    val feelsLike: Int,
    val humidity: Int,
    val windSpeed: Double,
    val sunrise: Long,
    val sunSet: Long
)
