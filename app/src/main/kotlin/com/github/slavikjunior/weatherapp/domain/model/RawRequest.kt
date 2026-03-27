package com.github.slavikjunior.weatherapp.domain.model

@JvmInline
internal value class RawRequest(val city: String) {

    init {
        require(city.isNotBlank()) { "City can not be blank" }
    }
}
