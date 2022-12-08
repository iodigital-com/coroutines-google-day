package com.io.reactivecoroutines.weather;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.time.LocalDate;

@RestController
@RequestMapping("/weather-infos/stream")
public class WeatherStreamController {

    private static final Logger LOGGER = Loggers.getLogger(WeatherStreamController.class);
    private final WeatherService weatherService;

    public WeatherStreamController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    Flux<WeatherInfo> getWeatherStream() {
        return Flux.interval(Duration.ofMillis(10))
                .map(dayToAdd -> LocalDate.of(1995, 1, 1).plusDays(dayToAdd))
                .takeUntil(date -> date.isAfter(LocalDate.of(2015, 12, 28)))
                .map(
                        dateToGet -> new WeatherInfo(
                                "Europe",
                                "Denmark",
                                "",
                                "Copenhagen",
                                dateToGet,
                                null
                        ))
                .flatMap(weatherService::queryOneByExample)
                .log();
    }
}
