package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record ForecastDay(@NotNull LocalDate date, @JsonProperty("day") @NotNull Temperature temperature) {
}
