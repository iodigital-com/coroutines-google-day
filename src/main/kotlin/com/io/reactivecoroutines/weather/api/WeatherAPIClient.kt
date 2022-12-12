package com.io.reactivecoroutines.weather.api

import com.io.reactivecoroutines.weather.WeatherInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.Exceptions
import reactor.core.publisher.Mono

@Component
class WeatherAPIClient(
    private val http: WebClient,
    @param:Value("\${weatherapi.host}") private val host: String,
    @param:Value("\${weatherapi.api-key}") private val apiKey: String
) {

    companion object {
        private val log = LoggerFactory.getLogger(WeatherAPIClient::class.java)
    }

    fun getWeather(info: WeatherInfo): Mono<WeatherAPIResponse> {
        val maybeQuery = info.city?.takeIf { it.isNotBlank() }
            ?: info.state?.takeIf { it.isNotBlank() }
            ?: info.country?.takeIf { it.isNotBlank() }
            ?: info.region?.takeIf { it.isNotBlank() }

        return maybeQuery?.let { query ->
            val forecast = http
                .get()
                .uri("$host/v1/forecast.json?key=$apiKey&q=$query&days=7")
                .exchangeToMono<WeatherAPIResponse?> { it.bodyToMono() }
                .doFirst { log.info("Getting weather forecast for {}", query) }
                .doOnError {
                    log.error("Cannot get weather forecast for $query", it)
                    Exceptions.propagate(it)
                }
                .doOnSuccess { response ->
                    log.info(
                        "Weather forecast for query {}: {}",
                        query,
                        response
                    )
                }

            info.localDate?.let {
                val localDate = info.localDate
                forecast.flatMap { f: WeatherAPIResponse ->
                    Mono
                        .justOrEmpty(f.forecast.days.firstOrNull { it.date == localDate })
                        .switchIfEmpty(Mono.error(IllegalArgumentException("No weather data found for query $query on $localDate")))
                        .map { day -> WeatherAPIResponse(f.location, Forecast(listOf(day))) }
                }
            } ?: forecast
        } ?: Mono.error(IllegalArgumentException("Invalid query"))
    }
}