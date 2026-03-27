package com.github.slavikjunior.weatherapp.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class OpenWeatherApiKeyInterceptor(
    private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter(API_KEY_FIELD, apiKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val API_KEY_FIELD = "appid"
    }
}
