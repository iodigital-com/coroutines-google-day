package com.io.reactivecoroutines.weather

import com.io.reactivecoroutines.weather.api.WeatherAPIClient
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.kotlin.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class WeatherServiceTest {
    @Test
    fun `if people can wear t-shirt today`() = runTest {
        val now = Instant.now()
        val fixedClock = Clock.fixed(now, ZoneOffset.UTC)
        val weatherRepository = mock(WeatherRepository::class.java)
        val weatherAPIClient = mock(WeatherAPIClient::class.java)
        val service = WeatherService(fixedClock, weatherRepository, weatherAPIClient)

        assertEquals(
            listOf("Yes, Akif can wear a t-shirt anywhere, any time."),
            service.canWearTShirtToday("Akif", "Amsterdam")
        )

        assertEquals(
            listOf("Yes, Akif can wear a t-shirt anywhere, any time."),
            service.canWearTShirtToday("akif", "Istanbul")
        )

        `when`(weatherRepository.queryWeatherInfoByCity("Amsterdam")).thenReturn(
            flowOf(
                WeatherInfo(
                    id = null,
                    region = "",
                    country = "",
                    state = "",
                    city = "Amsterdam",
                    localDate = LocalDate.ofInstant(now, ZoneOffset.UTC),
                    avgTemperature = "50"
                )
            )
        )
        `when`(weatherRepository.queryWeatherInfoByCity("Istanbul")).thenReturn(
            flowOf(
                WeatherInfo(
                    id = null,
                    region = "",
                    country = "",
                    state = "",
                    city = "Istanbul",
                    localDate = LocalDate.ofInstant(now, ZoneOffset.UTC),
                    avgTemperature = "70"
                )
            )
        )

        `when`(weatherRepository.queryWeatherInfoByCity("Berlin")).thenReturn(flowOf())

        `when`(weatherAPIClient.getWeather(any())).thenReturn(flowOf())

        `when`(weatherRepository.save(any())).thenReturn(flowOf())

        assertEquals(
            listOf("No, Ayse cannot wear a t-shirt in Amsterdam today."),
            service.canWearTShirtToday("Ayse", "Amsterdam")
        )

        assertEquals(
            listOf("Yes, Arno can wear a t-shirt in Istanbul today."),
            service.canWearTShirtToday("Arno", "Istanbul")
        )

        assertEquals(
            listOf("Don't know if Leo can wear a t-shirt in Berlin today because weather data is missing"),
            service.canWearTShirtToday("Leo", "Berlin")
        )
    }
}
