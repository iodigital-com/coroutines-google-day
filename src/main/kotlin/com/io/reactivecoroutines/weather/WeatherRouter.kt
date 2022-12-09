package com.io.reactivecoroutines.weather

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration(proxyBeanMethods = false)
class WeatherRouter {
    @Bean
    fun route(weatherSearchHandler: WeatherSearchHandler): RouterFunction<ServerResponse> {

        return RouterFunctions
            .route(
                POST("/weather-infos/search/")
                    .and(accept(MediaType.APPLICATION_JSON)), weatherSearchHandler::searchByExample
            )
            .andRoute(
                POST("/weather-infos/search-one/")
                    .and(accept(MediaType.APPLICATION_JSON)), weatherSearchHandler::searchOneByExample
            )
            .andRoute(
                GET("/weather-infos/random/")
                    .and(accept(MediaType.APPLICATION_JSON)), weatherSearchHandler::searchRandomOneByExample
            )
            .andRoute(
                GET("/weather-infos/stream/")
                    .and(accept(MediaType.APPLICATION_JSON)), weatherSearchHandler::getWeatherStream
            )
    }
}
