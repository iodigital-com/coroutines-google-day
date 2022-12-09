package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public record Location(
        @NotNull @JsonProperty("name") String city,
        @NotNull String region,
        @NotNull String country
) {
}
