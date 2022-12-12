package com.io.reactivecoroutines.weather.api

import com.io.reactivecoroutines.weather.WeatherInfo

data class WeatherAPIResponse(val location: Location, val forecast: Forecast) {
    fun toWeatherInfoList(): List<WeatherInfo> {
        return forecast.days.stream().map { f ->
            with(location) {
                WeatherInfo(
                    id = null,
                    region = region,
                    country = country,
                    state = null,
                    city = city,
                    localDate = f.date,
                    avgTemperature = "${f.temperature.avgF}"
                )
            }
        }.toList()
    }
}