package com.io.reactivecoroutines.weather.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private val log: Logger = LoggerFactory.getLogger(WeatherAPIClient::class.java)

@Component
class WeatherAPIClient(
    private val http: WebClient,
    @Value("\${weatherapi.host}") private val host: String,
    @Value("\${weatherapi.api-key}") private val apiKey: String
) {

    fun getCurrentWeatherIn(city: String): Mono<WeatherAPIResponse> {
        return http
            .get()
            .uri("%s/v1/current.json?key=%s&q=%s&days=7".format(host, apiKey, city))
            .exchangeToMono { response -> response.bodyToMono<WeatherAPIResponse>() }
            .doFirst { log.info("Getting current weather for {}", city) }
            .doOnError { e -> log.error("Cannot get current weather for %s".format(city), e) }
            .doOnSuccess { response -> log.info("Current weather for city {}: {}", city, response) }
    }
}
