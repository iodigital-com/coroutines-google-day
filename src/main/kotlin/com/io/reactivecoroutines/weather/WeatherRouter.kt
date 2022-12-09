package com.io.reactivecoroutines.weather

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration(proxyBeanMethods = false)
class WeatherRouter {
    @Bean
    fun router(weatherSearchHandler: WeatherSearchHandler) = coRouter {
        POST("/weather-infos/search/", weatherSearchHandler::searchByExample)
        POST("/weather-infos/search-one/", weatherSearchHandler::searchOneByExample)
        GET("/weather-infos/random/", weatherSearchHandler::searchRandomOneByExample)
        GET("/weather-infos/stream/", weatherSearchHandler::getWeatherStream)
    }
}
