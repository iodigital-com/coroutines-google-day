package com.io.reactivecoroutines.weather

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/weather-infos")
class WeatherController(private val weatherService: WeatherService) {
    @GetMapping
    suspend fun allWeather(): List<WeatherInfo> {
        return weatherService.getAllWeather()
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: Long): WeatherInfo {
        return weatherService.getWeatherInfoById(id)
    }

    @GetMapping("/can-wear-t-shirt-today")
    suspend fun canWearTShirtToday(@RequestParam name: String, city: String): List<String> {
        return weatherService.canWearTShirtToday(name, city)
    }
}