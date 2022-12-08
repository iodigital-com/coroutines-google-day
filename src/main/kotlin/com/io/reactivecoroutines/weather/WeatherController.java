package com.io.reactivecoroutines.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping("/weather-infos")
public class WeatherController {
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Flux<WeatherInfo> getAllWeather() {
        return weatherService.getAllWeather();
    }

    @GetMapping("/{id}")
    public Mono<WeatherInfo> getById(String id) {
        return weatherService.getWeatherInfoById(id);
    }

    @GetMapping("/current")
    public Mono<WeatherInfo> getCurrentWeatherIn(@RequestParam final String city) {
        return weatherService.getCurrentWeatherIn(city);
    }
}
