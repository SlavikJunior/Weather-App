package com.github.slavikjunior.weatherapp.domain.model

@JvmInline
internal value class RawCity(val city: String) {

    init {
        require(city.isNotBlank()) { "City can not be blank" }
    }
}
