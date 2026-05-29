package com.github.SlavikJunior.weatherapp

import com.github.slavikjunior.weatherapp.domain.model.DataSource
import com.github.slavikjunior.weatherapp.domain.model.RawCity
import com.github.slavikjunior.weatherapp.domain.model.WeatherData
import com.github.slavikjunior.weatherapp.domain.model.WeatherResult
import com.github.slavikjunior.weatherapp.domain.repository.WeatherDataRepository
import com.github.slavikjunior.weatherapp.domain.usecase.GetCurrentWeatherDataByLocationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCurrentWeatherDataByLocationUseCaseTest {

    private val expectedWeatherData = WeatherData(
        name = "London",
        description = "clear sky",
        icon = "01d",
        temp = 20,
        feelsLike = 18,
        humidity = 60,
        windSpeed = 3.5,
        sunrise = 1234567L,
        sunSet = 1234599L
    )
    private val expectedResult = WeatherResult(data = expectedWeatherData, source = DataSource.SERVER)

    @Test
    fun `invoke calls repository once and returns correct WeatherResult`() = runTest {
        val mockRepo = mockk<WeatherDataRepository>()
        coEvery { mockRepo.getCurrentWeather("London") } returns expectedResult

        val useCase = GetCurrentWeatherDataByLocationUseCase(mockRepo)
        val result = useCase(RawCity("London"))

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { mockRepo.getCurrentWeather("London") }
    }
}
