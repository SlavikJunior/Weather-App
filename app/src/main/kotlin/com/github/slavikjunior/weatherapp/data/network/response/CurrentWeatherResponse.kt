package com.github.slavikjunior.weatherapp.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("coord") val coordinates: Coordinates,
    @SerialName("weather") val weather: List<Weather>,
    @SerialName("base") val base: String,
    @SerialName("main") val main: Main,
    @SerialName("visibility") val visibility: Int? = null,
    @SerialName("wind") val wind: Wind,
    @SerialName("clouds") val clouds: Clouds,
    @SerialName("rain") val rain: Rain? = null,
    @SerialName("snow") val snow: Snow? = null,
    @SerialName("dt") val dt: Long,
    @SerialName("sys") val system: System,
    @SerialName("timezone") val timezone: Long,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("cod") val code: Int
)

@Serializable
data class Coordinates(
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double
)

@Serializable
data class Weather(
    @SerialName("id") val id: Int,
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class Main(
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("grnd_level") val grndLevel: Int? = null,
    @SerialName("humidity") val humidity: Int,
    @SerialName("pressure") val pressure: Int,
    @SerialName("sea_level") val seaLevel: Int? = null,
    @SerialName("temp") val temp: Double,
    @SerialName("temp_max") val tempMax: Double,
    @SerialName("temp_min") val tempMin: Double
)

@Serializable
data class Wind(
    @SerialName("speed") val speed: Double,
    @SerialName("deg") val deg: Int? = null,
    @SerialName("gust") val gust: Double? = null
)

@Serializable
data class Clouds(
    @SerialName("all") val all: Int
)

@Serializable
data class Rain(
    @SerialName("1h") val perHour: Double? = null
)

@Serializable
data class Snow(
    @SerialName("1h") val perHour: Double? = null
)

@Serializable
data class System(
    @SerialName("type") val type: Int? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("sunrise") val sunrise: Long,
    @SerialName("sunset") val sunset: Long
)
