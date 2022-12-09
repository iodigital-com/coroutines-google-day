package com.io.reactivecoroutines.weather;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping("/weather-infos")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Flux<WeatherInfo> getAllWeather() {
        return weatherService.getAllWeather();
    }

    @GetMapping("/{id}")
    public Mono<WeatherInfo> getById(@PathVariable Long id) {
        return weatherService.getWeatherInfoById(id);
    }

    @GetMapping("/can-wear-t-shirt-today")
    public Mono<String> canWearTShirtToday(@RequestParam final String name, final String city) {
        return weatherService.canWearTShirtToday(name, city);
    }
}
