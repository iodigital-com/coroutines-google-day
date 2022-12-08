package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.io.reactivecoroutines.weather.WeatherInfo;

public record WeatherAPIResponse(Location location, @JsonProperty("current") CurrentWeather weather) {
    public WeatherInfo toWeatherInfo() {
        final var time = location.time();
        return new WeatherInfo(
                null,
                location.region(),
                location.country(),
                null,
                location.city(),
                time.getMonthValue(),
                time.getDayOfMonth(),
                time.getYear(),
                "%.2f C / %.2f F".formatted(weather.celcius(), weather.fahrenheit())
        );
    }
}