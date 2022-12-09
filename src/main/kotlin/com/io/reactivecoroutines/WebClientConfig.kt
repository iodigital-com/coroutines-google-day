package com.io.reactivecoroutines

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.client.reactive.ReactorResourceFactory
import org.springframework.web.reactive.function.client.WebClient

import java.time.Duration

@Configuration
class WebClientConfig {
    @Bean
    fun resourceFactory(): ReactorResourceFactory = ReactorResourceFactory()

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(resourceFactory()) { client ->
                client.responseTimeout(Duration.ofSeconds(10))
            })
            .build()
    }
}