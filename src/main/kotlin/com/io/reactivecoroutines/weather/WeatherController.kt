package com.io.reactivecoroutines.weather

import kotlinx.coroutines.flow.Flow
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/weather-infos")
class WeatherController(
    private val weatherService: WeatherService,
) {

    @GetMapping
    suspend fun getAllWeather(): Flow<WeatherInfo> {
        return weatherService.getAllWeather()
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: Long): Flow<WeatherInfo> {
        return weatherService.getWeatherInfoById(id)
    }

    @GetMapping("/current")
    suspend fun getCurrentWeatherIn(@RequestParam city: String): Flow<WeatherInfo> {
        return weatherService.getCurrentWeatherIn(city)
    }
}
