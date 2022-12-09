package com.io.reactivecoroutines.weather.api

import com.fasterxml.jackson.annotation.JsonProperty

data class CurrentWeather(
    @JsonProperty("temp_c") val celcius: Double,
    @JsonProperty("temp_f") val fahrenheit: Double,
)
