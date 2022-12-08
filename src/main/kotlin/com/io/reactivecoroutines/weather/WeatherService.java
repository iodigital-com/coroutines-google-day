package com.io.reactivecoroutines.weather;

import com.io.reactivecoroutines.weather.api.WeatherAPIClient;
import com.io.reactivecoroutines.weather.api.WeatherAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Service
public class WeatherService {

    private static final Logger LOGGER = Loggers.getLogger(WeatherService.class);

    private final WeatherRepository weatherRepository;
    private final WeatherAPIClient weatherAPI;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, WeatherAPIClient weatherAPI) {
        this.weatherRepository = weatherRepository;
        this.weatherAPI = weatherAPI;
    }

    public Flux<WeatherInfo> getAllWeather() {
        LOGGER.debug("WeatherService getAllWeather");
        return weatherRepository.findAll();
    }

    public Mono<WeatherInfo> getWeatherInfoById(Long id) {
        LOGGER.debug("WeatherService getWeatherInfoById");
        return weatherRepository.findById(id);
    }

    public Flux<WeatherInfo> queryWeatherInfoByCity(String city) {
        LOGGER.debug("WeatherService queryWeatherInfoByCity");
        return weatherRepository.queryWeatherInfoByCity(city);
    }

    public Flux<WeatherInfo> queryByExample(WeatherInfo weatherInfo) {
        LOGGER.debug("WeatherService queryByExample");
        return weatherRepository.findAll(Example.of(weatherInfo));
    }

    public Mono<WeatherInfo> queryOneByExample(WeatherInfo weatherInfo) {
        LOGGER.debug("WeatherService querySingleByExample");
        return weatherRepository.findOne(Example.of(weatherInfo));
    }

    public Mono<WeatherInfo> getCurrentWeatherIn(final String city) {
        return weatherAPI.getCurrentWeatherIn(city).map(WeatherAPIResponse::toWeatherInfo);
    }
}
