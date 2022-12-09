package com.io.reactivecoroutines.weather.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.io.reactivecoroutines.weather.WeatherInfo

import java.time.LocalDate

data class WeatherAPIResponse(
    val location: Location,
    @JsonProperty("current") val weather: CurrentWeather,
) {
    fun toWeatherInfo(): WeatherInfo {
        val time = location.time()
        return WeatherInfo(
            id = null,
            region = location.region,
            country = location.country,
            state = "",
            city = location.city,
            localDate = LocalDate.of(
                time.getYear(),
                time.getMonthValue(),
                time.getDayOfMonth()
            ),
            avgTemperature = "%.2f C / %.2f F".format(weather.celcius, weather.fahrenheit)
        )
    }
}