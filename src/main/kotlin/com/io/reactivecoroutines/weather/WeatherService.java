package com.io.reactivecoroutines.weather;

import com.io.reactivecoroutines.weather.api.WeatherAPIClient;
import com.io.reactivecoroutines.weather.api.WeatherAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherAPIClient weatherAPI;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, WeatherAPIClient weatherAPI) {
        this.weatherRepository = weatherRepository;
        this.weatherAPI = weatherAPI;
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

    public Mono<WeatherInfo> getCurrentWeatherIn(final String city) {
        return weatherAPI.getCurrentWeatherIn(city).map(WeatherAPIResponse::toWeatherInfo);
    }
}
