package com.io.reactivecoroutines.weather.api;

import com.io.reactivecoroutines.weather.WeatherInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record WeatherAPIResponse(@NotNull Location location, @NotNull Forecast forecast) {
    public List<WeatherInfo> toWeatherInfoList() {
        final var region = location.region();
        final var country = location.country();
        final var city = location.city();
        return forecast.days().stream().map(f ->
                new WeatherInfo(
                        region,
                        country,
                        null,
                        city,
                        f.date(),
                        "%.2f".formatted(f.temperature().avgF())
                )
        ).toList();
    }
}

