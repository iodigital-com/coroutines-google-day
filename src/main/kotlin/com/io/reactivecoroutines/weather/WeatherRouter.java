package com.io.reactivecoroutines.weather;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class WeatherRouter {
    @Bean
    public RouterFunction<ServerResponse> route(WeatherSearchHandler weatherSearchHandler) {

        return RouterFunctions
                .route(POST("/weather-infos/search/")
                        .and(accept(MediaType.APPLICATION_JSON)), weatherSearchHandler::searchByExample)
                .andRoute(POST("/weather-infos/search-one/")
                        .and(accept(MediaType.APPLICATION_JSON)), weatherSearchHandler::searchOneByExample)
                .andRoute(GET("/weather-infos/random/")
                        .and(accept(MediaType.APPLICATION_JSON)), weatherSearchHandler::searchRandomOneByExample);
    }
}
