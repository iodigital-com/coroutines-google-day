package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentWeather(@JsonProperty("temp_c") double celcius, @JsonProperty("temp_f") double fahrenheit) {
}
