package com.io.reactivecoroutines.weather

import com.io.reactivecoroutines.weather.api.WeatherAPIClient
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.Logger
import reactor.util.Loggers

@Service
class WeatherService(
    private val weatherRepository: WeatherRepository,
    private val weatherAPI: WeatherAPIClient,
) {
    private val log: Logger = Loggers.getLogger(WeatherService::class.java)

    fun getAllWeather(): Flux<WeatherInfo> {
        log.debug("WeatherService getAllWeather")
        return weatherRepository.findAll()
    }

    fun getWeatherInfoById(id: Long): Mono<WeatherInfo> {
        log.debug("WeatherService getWeatherInfoById")
        return weatherRepository.findById(id)
    }

    fun queryWeatherInfoByCity(city: String): Flux<WeatherInfo> {
        log.debug("WeatherService queryWeatherInfoByCity")
        return weatherRepository.queryWeatherInfoByCity(city)
    }

    fun queryByExample(weatherInfo: WeatherInfo): Flux<WeatherInfo> {
        log.debug("WeatherService queryByExample")
        return weatherRepository.findAll(Example.of(weatherInfo))
    }

    fun queryOneByExample(weatherInfo: WeatherInfo): Mono<WeatherInfo> {
//        LOGGER.debug("WeatherService querySingleByExample")
        return weatherRepository.findOne(Example.of(weatherInfo))
    }

    fun getCurrentWeatherIn(city: String): Mono<WeatherInfo> {
        return weatherAPI.getCurrentWeatherIn(city).map { it.toWeatherInfo() }
    }
}
