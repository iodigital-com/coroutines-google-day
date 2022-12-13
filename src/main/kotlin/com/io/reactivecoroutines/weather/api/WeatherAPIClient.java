package com.io.reactivecoroutines.weather.api;

import com.io.reactivecoroutines.weather.WeatherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class WeatherAPIClient {
    private static final Logger log = LoggerFactory.getLogger(WeatherAPIClient.class);

    private final WebClient http;
    private final String host;
    private final String apiKey;

    public WeatherAPIClient(
            final WebClient http,
            @Value("${weatherapi.host}") final String host,
            @Value("${weatherapi.api-key}") final String apiKey
    ) {
        this.http = http;
        this.host = host;
        this.apiKey = apiKey;
    }

    public Mono<WeatherAPIResponse> getWeather(final WeatherInfo info) {
        final var maybeQuery = Optional.ofNullable(info.getCity()).filter(s -> !s.isBlank()).or(() ->
                Optional.ofNullable(info.getState()).filter(s -> !s.isBlank()).or(() ->
                        Optional.ofNullable(info.getCountry()).filter(s -> !s.isBlank()).or(() ->
                                Optional.ofNullable(info.getRegion()).filter(s -> !s.isBlank())
                        )
                )
        );

        if (maybeQuery.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Invalid query"));
        }

        final var query = maybeQuery.get();

        final var forecast = http
                .get()
                .uri("%s/v1/forecast.json?key=%s&q=%s&days=7".formatted(host, apiKey, query))
                .exchangeToMono(response -> response.bodyToMono(WeatherAPIResponse.class))
                .doFirst(() -> log.info("Getting weather forecast for {}", query))
                .doOnError(e -> log.error("Cannot get weather forecast for %s".formatted(query), e))
                .doOnSuccess(response -> log.info("Weather forecast for query {}: {}", query, response));

        if (info.getLocalDate() == null) {
            return forecast;
        }

        final var localDate = info.getLocalDate();

        return forecast.flatMap(f ->
            Mono
                    .justOrEmpty(f.forecast().days().stream().filter(day -> day.date().equals(localDate)).findFirst())
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("No weather data found for query %s on %s".formatted(query, localDate))))
                    .map(day -> new WeatherAPIResponse(f.location(), new Forecast(List.of(day))))
        );
    }
}
