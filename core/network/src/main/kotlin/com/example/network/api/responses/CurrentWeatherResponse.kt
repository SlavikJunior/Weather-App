package com.example.network.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class CurrentWeatherResponse(
    @SerialName("coord") val coordinates: Coordinates,
    @SerialName("weather") val weather: Weather,
    @SerialName("base") val base: String,
    @SerialName("main") val main: Main,
    @SerialName("visibility") val visibility: Int,
    @SerialName("wind") val wind: Wind,
    @SerialName("clouds") val clouds: Clouds,
    @SerialName("rain") val rain: Rain,
    @SerialName("snow") val snow: Snow,
    @SerialName("dt") val dt: Long,
    @SerialName("sys") val system: System,
    @SerialName("timezone") val timezone: Long,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("code") val code: Int
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
    @SerialName("grnd_level") val grndLevel: Int,
    @SerialName("humidity") val humidity: Int,
    @SerialName("pressure") val pressure: Int,
    @SerialName("sea_level") val seaLevel: Int,
    @SerialName("temp") val temp: Double,
    @SerialName("temp_max") val tempMax: Double,
    @SerialName("temp_min") val tempMin: Double
)

@Serializable
data class Wind(
    @SerialName("speed") val speed: Int,
    @SerialName("deg") val deg: Int,
    @SerialName("gust") val gust: Int
)

@Serializable
@JvmInline
value class Clouds(@SerialName("all") val all: Int)

@Serializable
@JvmInline
value class Rain(@SerialName("1h") val perHour: Int)

@Serializable
@JvmInline
value class Snow(@SerialName("1h") val perHour: Int)

@Serializable
data class System(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)