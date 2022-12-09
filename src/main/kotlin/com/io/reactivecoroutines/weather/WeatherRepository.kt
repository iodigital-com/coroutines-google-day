package com.io.reactivecoroutines.weather

import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.stereotype.Repository

@Repository
interface WeatherRepository : CoroutineSortingRepository<WeatherInfo, Long>,
    ReactiveQueryByExampleExecutor<WeatherInfo> {
    suspend fun save(weatherInfo: WeatherInfo): Flow<WeatherInfo>

    suspend fun findById(id: Long): Flow<WeatherInfo>

    @NotNull
    suspend fun deleteById(id: Long): Flow<Unit>

    suspend fun queryWeatherInfoByCity(city: String): Flow<WeatherInfo>
}
