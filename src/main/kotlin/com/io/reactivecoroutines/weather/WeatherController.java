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

    @GetMapping("/current")
    public Mono<WeatherInfo> getCurrentWeatherIn(@RequestParam final String city) {
        return weatherService.getCurrentWeatherIn(city);
    }
}
