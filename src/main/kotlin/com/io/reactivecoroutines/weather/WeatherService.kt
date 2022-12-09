package com.io.reactivecoroutines.weather

import com.io.reactivecoroutines.weather.api.WeatherAPIClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.util.Logger
import reactor.util.Loggers

@Service
class WeatherService(
    private val weatherRepository: WeatherRepository,
    private val weatherAPI: WeatherAPIClient,
) {
    private val log: Logger = Loggers.getLogger(WeatherService::class.java)

    suspend fun getAllWeather(): Flow<WeatherInfo> {
        log.debug("WeatherService getAllWeather")
        return weatherRepository.findAll(Sort.unsorted())
    }

    suspend fun getWeatherInfoById(id: Long): Flow<WeatherInfo> {
        log.debug("WeatherService getWeatherInfoById")
        return weatherRepository.findById(id)
    }

    suspend fun queryWeatherInfoByCity(city: String): Flow<WeatherInfo> {
        log.debug("WeatherService queryWeatherInfoByCity")
        return weatherRepository.queryWeatherInfoByCity(city)
    }

    suspend fun queryByExample(weatherInfo: WeatherInfo): Flow<WeatherInfo> {
        log.debug("WeatherService queryByExample")
        return weatherRepository.findAll(Example.of(weatherInfo)).asFlow()
    }

    suspend fun queryOneByExample(weatherInfo: WeatherInfo): Flow<WeatherInfo> {
        log.debug("WeatherService querySingleByExample")
        return weatherRepository.findOne(Example.of(weatherInfo)).asFlow()
    }

    suspend fun getCurrentWeatherIn(city: String): Flow<WeatherInfo> {
        return weatherAPI.getCurrentWeatherIn(city).map { it.toWeatherInfo() }
    }
}
