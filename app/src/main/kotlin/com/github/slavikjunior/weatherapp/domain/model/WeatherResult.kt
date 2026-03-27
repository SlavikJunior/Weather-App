package com.github.slavikjunior.weatherapp.domain.model

internal data class WeatherResult(
    val data: WeatherData,
    val source: DataSource
)
