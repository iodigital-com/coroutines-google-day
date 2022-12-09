package com.io.reactivecoroutines.weather

import org.jetbrains.annotations.NotNull
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface WeatherRepository : ReactiveCrudRepository<WeatherInfo, String>, ReactiveQueryByExampleExecutor<WeatherInfo> {

    override fun <S : WeatherInfo> save(weatherInfo: S): Mono<S>

    @NotNull
    override fun findAll(): Flux<WeatherInfo>

    fun findById(id: Long): Mono<WeatherInfo>

    @NotNull
    fun deleteById(id: Long): Mono<Unit>

    fun queryWeatherInfoByCity(city: String): Flux<WeatherInfo>
}
