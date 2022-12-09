package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Temperature(
        @JsonProperty("maxtemp_c") double maxC,
        @JsonProperty("maxtemp_f") double maxF,
        @JsonProperty("mintemp_c") double minC,
        @JsonProperty("mintemp_f") double minF,
        @JsonProperty("avgtemp_c") double avgC,
        @JsonProperty("avgtemp_f") double avgF
) {
}
