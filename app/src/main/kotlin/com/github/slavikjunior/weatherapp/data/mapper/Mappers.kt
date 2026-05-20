package com.github.slavikjunior.weatherapp.data.mapper

import com.github.slavikjunior.weatherapp.data.network.response.CurrentWeatherResponse
import com.github.slavikjunior.weatherapp.domain.model.WeatherData

internal fun CurrentWeatherResponse.toDomainModel(): WeatherData {
    val weather = this.weather.firstOrNull()
    return WeatherData(
        name = name,
        description = weather?.description.orEmpty(),
        icon = weather?.icon.orEmpty(),
        temp = main.temp.toInt(),
        feelsLike = main.feelsLike.toInt(),
        humidity = main.humidity,
        windSpeed = wind.speed,
        sunrise = system.sunrise,
        sunSet = system.sunset
    )
}