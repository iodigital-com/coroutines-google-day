package com.io.reactivecoroutines.weather;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Validated
@RequestMapping("/weather-infos/cities")
public class WeatherCityController {
    private final WeatherService weatherService;

    public WeatherCityController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Flux<WeatherInfo> getAllWeatherByCity(@RequestParam String city) {
        return weatherService.queryWeatherInfoByCity(city);
    }
}
