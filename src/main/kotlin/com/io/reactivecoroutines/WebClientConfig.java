package com.io.reactivecoroutines;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {
  @Bean
  public ReactorResourceFactory resourceFactory() {
    return new ReactorResourceFactory();
  }

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(resourceFactory(), client -> client.responseTimeout(Duration.ofSeconds(10))))
      .build();
  }
}