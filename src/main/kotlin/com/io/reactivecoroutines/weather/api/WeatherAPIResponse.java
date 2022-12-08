package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.io.reactivecoroutines.weather.WeatherInfo;

import java.time.LocalDate;

public record WeatherAPIResponse(Location location, @JsonProperty("current") CurrentWeather weather) {
    public WeatherInfo toWeatherInfo() {
        final var time = location.time();
        return new WeatherInfo(
                location.region(),
                location.country(),
                null,
                location.city(),
                LocalDate.of(
                        time.getYear(),
                        time.getMonthValue(),
                        time.getDayOfMonth()
                ),
                "%.2f C / %.2f F".formatted(weather.celcius(), weather.fahrenheit())
        );
    }
}