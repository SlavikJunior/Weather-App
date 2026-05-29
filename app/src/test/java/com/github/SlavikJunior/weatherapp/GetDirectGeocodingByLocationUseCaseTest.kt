package com.github.SlavikJunior.weatherapp

import com.github.slavikjunior.weatherapp.domain.model.LocationData
import com.github.slavikjunior.weatherapp.domain.model.RawCity
import com.github.slavikjunior.weatherapp.domain.repository.WeatherDataRepository
import com.github.slavikjunior.weatherapp.domain.usecase.GetDirectGeocodingByLocationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDirectGeocodingByLocationUseCaseTest {

    private val expectedLocation = LocationData(latitude = 51.5074, longitude = -0.1278)

    @Test
    fun `invoke calls repository once and returns correct LocationData`() = runTest {
        val mockRepo = mockk<WeatherDataRepository>()
        coEvery { mockRepo.getLocationByCity("London") } returns expectedLocation

        val useCase = GetDirectGeocodingByLocationUseCase(mockRepo)
        val result = useCase(RawCity("London"))

        assertEquals(expectedLocation, result)
        coVerify(exactly = 1) { mockRepo.getLocationByCity("London") }
    }
}
