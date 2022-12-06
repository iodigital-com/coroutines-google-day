package com.io.reactivecoroutines.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;


    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Flux<WeatherInfo> getAllWeather() {
        return weatherRepository.findAll();
    }

    public Mono<WeatherInfo> getWeatherInfoById(String id) {
        return weatherRepository.findById(id);
    }

    public Flux<WeatherInfo> queryWeatherInfoByCity(String city) {
        return weatherRepository.queryWeatherInfoByCity(city);
    }
}
