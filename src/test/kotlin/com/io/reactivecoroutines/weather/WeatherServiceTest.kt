package com.io.reactivecoroutines.weather

import com.io.reactivecoroutines.weather.api.WeatherAPIClient
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class WeatherServiceTest {
    @Test
    fun `if people can wear t-shirt today`() {
        val now = Instant.now()
        val fixedClock = Clock.fixed(now, ZoneOffset.UTC)
        val weatherRepository = mock(WeatherRepository::class.java)
        val weatherAPIClient = mock(WeatherAPIClient::class.java)
        val service = WeatherService(fixedClock, weatherRepository, weatherAPIClient)

        StepVerifier
            .create(service.canWearTShirtToday("Akif", "Amsterdam"))
            .expectNext("Yes, Akif can wear a t-shirt anywhere, any time.")
            .verifyComplete()

        StepVerifier
            .create(service.canWearTShirtToday("akif", "Istanbul"))
            .expectNext("Yes, Akif can wear a t-shirt anywhere, any time.")
            .verifyComplete()

        `when`(weatherRepository.queryWeatherInfoByCity("Amsterdam")).thenReturn(
            Flux.just(
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
            Flux.just(
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

        `when`(weatherRepository.queryWeatherInfoByCity("Berlin")).thenReturn(Flux.empty())

        `when`(weatherAPIClient.getWeather(any())).thenReturn(Mono.empty())

        `when`(weatherRepository.saveAll(any<List<WeatherInfo>>())).thenReturn(Flux.empty())

        StepVerifier
            .create(service.canWearTShirtToday("Ayse", "Amsterdam"))
            .expectNext("No, Ayse cannot wear a t-shirt in Amsterdam today.")
            .verifyComplete()

        StepVerifier
            .create(service.canWearTShirtToday("Arno", "Istanbul"))
            .expectNext("Yes, Arno can wear a t-shirt in Istanbul today.")
            .verifyComplete()

        StepVerifier
            .create(service.canWearTShirtToday("Leo", "Berlin"))
            .expectNext("Don't know if Leo can wear a t-shirt in Berlin today because weather data is missing")
            .verifyComplete()
    }
}
