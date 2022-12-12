package com.io.reactivecoroutines.weather

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/weather-infos/cities/")
class WeatherCityController(
    private val weatherService: WeatherService
) {

    @GetMapping
    suspend fun getAllWeatherByCity(@RequestParam city: String): List<WeatherInfo> {
        return weatherService.queryWeatherInfoByCity(city)
    }
}
