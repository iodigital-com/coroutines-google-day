package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Forecast(@NotNull @JsonProperty("forecastday") List<ForecastDay> days) {
}
