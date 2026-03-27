package com.github.slavikjunior.weather_by_city.internal.models

@JvmInline
internal value class RawRequest(val city: String) {

    init {
        require(city.isNotBlank()) { "City can not be blank" }
    }
}