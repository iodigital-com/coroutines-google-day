package com.io.reactivecoroutines.weather;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WeatherRepository extends R2dbcRepository<WeatherInfo, String> {
    Mono<Void> save(Publisher<WeatherInfo> publisher);

    @NotNull
    Flux<WeatherInfo> findAll();

    @NotNull
    Mono<WeatherInfo> findById(@NotNull String id);

    @NotNull
    Mono<Void> deleteById(@NotNull String id);

    Flux<WeatherInfo> queryWeatherInfoByCity(@NotNull String city);
}
