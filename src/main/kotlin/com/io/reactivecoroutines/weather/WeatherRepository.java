package com.io.reactivecoroutines.weather;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WeatherRepository extends ReactiveCrudRepository<WeatherInfo, String>, ReactiveQueryByExampleExecutor<WeatherInfo> {
    @NotNull
    @Override
    <S extends WeatherInfo> Mono<S> save(@NotNull S weatherInfo);

    @NotNull
    Flux<WeatherInfo> findAll();

    @NotNull
    Mono<WeatherInfo> findById(@NotNull Long id);

    @NotNull
    Mono<Void> deleteById(@NotNull Long id);

    Flux<WeatherInfo> queryWeatherInfoByCity(@NotNull String city);
}
