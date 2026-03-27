package com.github.slavikjunior.weatherapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.slavikjunior.weatherapp.domain.model.WeatherData

@Entity(tableName = "weather_cache")
internal data class WeatherCacheEntity(
    @PrimaryKey val city: String,
    val name: String,
    val description: String,
    val icon: String,
    val temp: Int,
    val feelsLike: Int,
    val humidity: Int,
    val windSpeed: Double,
    val sunrise: Long,
    val sunset: Long,
    val cachedAt: Long
)

internal fun WeatherCacheEntity.toWeatherData(): WeatherData = WeatherData(
    name = name,
    description = description,
    icon = icon,
    temp = temp,
    feelsLike = feelsLike,
    humidity = humidity,
    windSpeed = windSpeed,
    sunrise = sunrise,
    sunSet = sunset
)

internal fun WeatherData.toCacheEntity(city: String, cachedAt: Long): WeatherCacheEntity =
    WeatherCacheEntity(
        city = city,
        name = name,
        description = description,
        icon = icon,
        temp = temp,
        feelsLike = feelsLike,
        humidity = humidity,
        windSpeed = windSpeed,
        sunrise = sunrise,
        sunset = sunSet,
        cachedAt = cachedAt
    )
