package com.io.reactivecoroutines.weather;

import com.io.reactivecoroutines.weather.api.WeatherAPIClient;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherServiceTest {
    @Test
    void canWearTShirtToday() {
        final var now = Instant.now();
        final var fixedClock = Clock.fixed(now, ZoneOffset.UTC);
        final var weatherRepository = mock(WeatherRepository.class);
        final var weatherAPIClient = mock(WeatherAPIClient.class);

        final var service = new WeatherService(fixedClock, weatherRepository, weatherAPIClient);

        StepVerifier
                .create(service.canWearTShirtToday("Akif", "Amsterdam"))
                .expectNext("Yes, Akif can wear a t-shirt anywhere, any time.")
                .verifyComplete();

        StepVerifier
                .create(service.canWearTShirtToday("akif", "Istanbul"))
                .expectNext("Yes, Akif can wear a t-shirt anywhere, any time.")
                .verifyComplete();

        when(weatherRepository.queryWeatherInfoByCity("Amsterdam")).thenReturn(
                Flux.just(
                        new WeatherInfo(
                                "",
                                "",
                                "",
                                "Amsterdam",
                                LocalDate.ofInstant(now, ZoneOffset.UTC),
                                "50"
                        )
                )
        );

        when(weatherRepository.queryWeatherInfoByCity("Istanbul")).thenReturn(
                Flux.just(
                        new WeatherInfo(
                                "",
                                "",
                                "",
                                "Istanbul",
                                LocalDate.ofInstant(now, ZoneOffset.UTC),
                                "70"
                        )
                )
        );

        when(weatherRepository.queryWeatherInfoByCity("Berlin")).thenReturn(Flux.empty());

        when(weatherAPIClient.getWeather(any())).thenReturn(Mono.empty());

        StepVerifier
                .create(service.canWearTShirtToday("Ayse", "Amsterdam"))
                .expectNext("No, Ayse cannot wear a t-shirt in Amsterdam today.")
                .verifyComplete();

        StepVerifier
                .create(service.canWearTShirtToday("Arno", "Istanbul"))
                .expectNext("Yes, Arno can wear a t-shirt in Istanbul today.")
                .verifyComplete();

        StepVerifier
                .create(service.canWearTShirtToday("Leo", "Berlin"))
                .expectNext("Don't know if Leo can wear a t-shirt in Berlin today because weather data is missing")
                .verifyComplete();
    }
}
