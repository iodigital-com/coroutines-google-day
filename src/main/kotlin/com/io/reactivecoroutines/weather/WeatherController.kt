package com.io.reactivecoroutines.weather

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@Validated
@RequestMapping("/weather-infos")
class WeatherController(private val weatherService: WeatherService) {
    @GetMapping
    fun allWeather(): Flux<WeatherInfo> {
        return weatherService.getAllWeather()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Mono<WeatherInfo> {
        return weatherService.getWeatherInfoById(id)
    }

    @GetMapping("/can-wear-t-shirt-today")
    fun canWearTShirtToday(@RequestParam name: String, city: String): Mono<String> {
        return weatherService.canWearTShirtToday(name, city)
    }
}