package com.github.SlavikJunior.weatherapp

import androidx.lifecycle.SavedStateHandle
import com.github.slavikjunior.weatherapp.domain.model.DataSource
import com.github.slavikjunior.weatherapp.domain.model.WeatherData
import com.github.slavikjunior.weatherapp.domain.model.WeatherResult
import com.github.slavikjunior.weatherapp.domain.usecase.GetCurrentWeatherDataByLocationUseCase
import com.github.slavikjunior.weatherapp.presentation.screen.weatherByCity.WeatherByCityEvent
import com.github.slavikjunior.weatherapp.presentation.screen.weatherByCity.WeatherByCityUiState
import com.github.slavikjunior.weatherapp.presentation.screen.weatherByCity.WeatherByCityViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherByCityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testWeatherData = WeatherData(
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

    @Test
    fun `when weather fetched successfully, uiState contains weatherData`() = runTest {
        val mockUseCase = mockk<GetCurrentWeatherDataByLocationUseCase>()
        coEvery { mockUseCase(any()) } returns WeatherResult(data = testWeatherData, source = DataSource.SERVER)

        val viewModel = WeatherByCityViewModel(SavedStateHandle(), mockUseCase)
        viewModel.reduce(WeatherByCityEvent.UpdateCurrentCityEvent("London"))
        viewModel.reduce(WeatherByCityEvent.GetCurrentWeatherEvent)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is WeatherByCityUiState.DefaultState)
        assertEquals(testWeatherData, (state as WeatherByCityUiState.DefaultState).weatherData)
    }

    @Test
    fun `when useCase throws, uiState is ErrorState`() = runTest {
        val mockUseCase = mockk<GetCurrentWeatherDataByLocationUseCase>()
        coEvery { mockUseCase(any()) } throws RuntimeException("Network error")

        val viewModel = WeatherByCityViewModel(SavedStateHandle(), mockUseCase)
        viewModel.reduce(WeatherByCityEvent.UpdateCurrentCityEvent("London"))
        viewModel.reduce(WeatherByCityEvent.GetCurrentWeatherEvent)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is WeatherByCityUiState.ErrorState)
    }
}
