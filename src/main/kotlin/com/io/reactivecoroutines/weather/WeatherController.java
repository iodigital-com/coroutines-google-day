package com.io.reactivecoroutines.weather;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping("/")
    public Flux<WeatherInfo> getAllWeather() {
        return weatherService.getAllWeather();
    }

    @RequestMapping("/{id}")
    public Mono<WeatherInfo> getById(@PathVariable Long id) {
        return weatherService.getWeatherInfoById(id);
    }
}
