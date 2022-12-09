package com.io.reactivecoroutines.weather.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.client.exchangeToFlow

private val log: Logger = LoggerFactory.getLogger(WeatherAPIClient::class.java)

@Component
class WeatherAPIClient(
    private val http: WebClient,
    @Value("\${weatherapi.host}") private val host: String,
    @Value("\${weatherapi.api-key}") private val apiKey: String
) {

    suspend fun getCurrentWeatherIn(city: String): Flow<WeatherAPIResponse> {
        return http
            .get()
            .uri("%s/v1/current.json?key=%s&q=%s&days=7".format(host, apiKey, city))
            .exchangeToFlow { response -> response.bodyToFlow<WeatherAPIResponse>() }
            .onStart { log.info("Getting current weather for {}", city) }
            .onCompletion { error ->
                error?.let {
                    log.error("Cannot get current weather for %s".format(city), error)
                } ?: kotlin.run {
                    log.info("Current weather for city $city")
                }
            }
    }
}
