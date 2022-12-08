package com.io.reactivecoroutines.weather;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class WeatherSearchHandler {

    private static final Logger LOGGER = Loggers.getLogger(WeatherSearchHandler.class);
    private final WeatherService weatherService;

    public WeatherSearchHandler(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    Mono<ServerResponse> searchByExample(ServerRequest req) {
        return req.bodyToMono(WeatherOptionFilters.class)
                .flatMap(filters -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(weatherService.queryByExample(
                                new WeatherInfo(
                                        filters.region,
                                        filters.country,
                                        filters.state,
                                        filters.city,
                                        filters.localDate,
                                        filters.avgTemperature
                                )
                        ), WeatherInfo.class)
                        .doOnNext(a -> LOGGER.debug("searchByExample onNext"))
                );
    }

    Mono<ServerResponse> searchOneByExample(ServerRequest req) {
        return req.bodyToMono(WeatherOptionFilters.class)
                .flatMap(filters -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                weatherService.queryOneByExample(
                                        new WeatherInfo(
                                                filters.region,
                                                filters.country,
                                                filters.state,
                                                filters.city,
                                                filters.localDate,
                                                filters.avgTemperature
                                        )
                                ), WeatherInfo.class)
                        .doOnNext(a -> LOGGER.debug("searchOneByExample onNext"))
                );
    }

    Mono<ServerResponse> searchRandomOneByExample(ServerRequest ignoredReq) {
        // bridge some blocking code here with a scheduler
        return Mono.fromCallable(this::getRandomCriteria)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(randomDateCriteria -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(weatherService.queryOneByExample(randomDateCriteria), WeatherInfo.class)
                        .doOnNext(a -> LOGGER.debug("searchRandomOneByExample onNext"))
                        .doOnSuccess(a -> LOGGER.debug("searchRandomOneByExample doOnSuccess"))
                );
    }

    private WeatherInfo getRandomCriteria() {
        // simulate a blocking implementation
        try {
            LOGGER.debug("getRandomCriteria");
            Thread.sleep(10);
            LOGGER.debug("getRandomCriteria after sleep");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Integer> daysList = IntStream.rangeClosed(1, 30).boxed().toList();
        int randomDayOfMonth = daysList.get(new Random().nextInt(daysList.size()));

        return new WeatherInfo(
                "Europe",
                "Finland",
                "",
                "Helsinki",
                LocalDate.of(2019, 7, randomDayOfMonth),
                null
        );
    }

    private record WeatherOptionFilters(
            String region,
            String country,
            String state,
            String city,
            LocalDate localDate,
            String avgTemperature
    ) {
    }
}
