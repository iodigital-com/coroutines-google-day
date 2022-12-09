package com.io.reactivecoroutines.weather;

import com.io.reactivecoroutines.weather.api.WeatherAPIClient;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class WeatherService {
    private static final Logger LOGGER = Loggers.getLogger(WeatherService.class);

    private final Clock clock;
    private final WeatherRepository weatherRepository;
    private final WeatherAPIClient weatherAPI;

    private static final double T_SHIRT_WEARING_THRESHOLD = 60.0;

    public WeatherService(final Clock clock, final WeatherRepository weatherRepository, final WeatherAPIClient weatherAPI) {
        this.clock = clock;
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
        return weatherRepository
                .queryWeatherInfoByCity(city)
                .switchIfEmpty(fetchAndSaveWeatherInfoByCity(city));
    }

    public Flux<WeatherInfo> queryByExample(WeatherInfo weatherInfo) {
        LOGGER.debug("WeatherService queryByExample");
        return weatherRepository
                .findAll(Example.of(weatherInfo))
                .switchIfEmpty(fetchAndSaveWeatherInfoByExample(weatherInfo));
    }

    public Mono<WeatherInfo> queryOneByExample(WeatherInfo weatherInfo) {
        LOGGER.debug("WeatherService querySingleByExample");
        return weatherRepository
                .findOne(Example.of(weatherInfo))
                .switchIfEmpty(fetchAndSaveWeatherInfoByExample(weatherInfo).next());
    }

    public Flux<WeatherInfo> fetchAndSaveWeatherInfoByCity(final String city) {
        return fetchAndSaveWeatherInfoByExample(new WeatherInfo(null, null, null, city, null, null));
    }

    public Flux<WeatherInfo> fetchAndSaveWeatherInfoByExample(final WeatherInfo info) {
        LOGGER.debug("WeatherService fetchAndSaveWeatherInfoByExample {}", info);
        return weatherAPI
                .getWeather(info)
                .flatMapMany(response -> Flux.fromIterable(response.toWeatherInfoList()))
                .buffer()
                .flatMap(weatherRepository::saveAll);
    }

    public Mono<String> canWearTShirtToday(final String name, final String city) {
        if (name.toLowerCase().equals("akif")) {
            return Mono.just("Yes, Akif can wear a t-shirt anywhere, any time.");
        }

        final var today = LocalDate.now(clock);

        return queryWeatherInfoByCity(city)
                .filter(w -> w.getLocalDate().equals(today))
                .next()
                .map(w -> {
                    if (Double.parseDouble(w.getAvgTemperature()) < T_SHIRT_WEARING_THRESHOLD) {
                        return "No, %s cannot wear a t-shirt in %s today.".formatted(name, city);
                    }

                    return "Yes, %s can wear a t-shirt in %s today.".formatted(name, city);
                })
                .switchIfEmpty(
                        Mono.just(
                                "Don't know if %s can wear a t-shirt in %s today because weather data is missing".formatted(name, city)
                        )
                );
    }
}
