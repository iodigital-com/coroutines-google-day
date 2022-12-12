package com.io.reactivecoroutines.weather

import com.io.reactivecoroutines.weather.api.WeatherAPIClient
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.Loggers
import java.time.Clock
import java.time.LocalDate
import java.util.*

@Service
class WeatherService(
    private val clock: Clock,
    private val weatherRepository: WeatherRepository,
    private val weatherAPI: WeatherAPIClient
) {

    companion object {
        private val log = Loggers.getLogger(WeatherService::class.java)
        private const val T_SHIRT_WEARING_THRESHOLD = 60.0
    }

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
        return weatherRepository
            .queryWeatherInfoByCity(city)
            .switchIfEmpty(fetchAndSaveWeatherInfoByCity(city))
    }

    fun queryByExample(weatherInfo: WeatherInfo): Flux<WeatherInfo> {
        log.debug("WeatherService queryByExample")
        return weatherRepository
            .findAll(Example.of(weatherInfo))
            .switchIfEmpty(fetchAndSaveWeatherInfoByExample(weatherInfo))
    }

    fun queryOneByExample(weatherInfo: WeatherInfo): Mono<WeatherInfo> {
        log.debug("WeatherService querySingleByExample")
        return weatherRepository
            .findOne(Example.of(weatherInfo))
            .switchIfEmpty(fetchAndSaveWeatherInfoByExample(weatherInfo).next())
    }

    fun fetchAndSaveWeatherInfoByCity(city: String?): Flux<WeatherInfo> {
        return fetchAndSaveWeatherInfoByExample(
            WeatherInfo(
                id = null,
                region = null,
                country = null,
                state = null,
                city = city,
                localDate = null,
                avgTemperature = null,
            )
        )
    }

    fun fetchAndSaveWeatherInfoByExample(info: WeatherInfo): Flux<WeatherInfo> {
        log.debug("WeatherService fetchAndSaveWeatherInfoByExample {}", info)
        return weatherAPI
            .getWeather(info)
            .flatMapMany { response ->
                Flux.fromIterable(
                    response.toWeatherInfoList()
                )
            }
            .switchIfEmpty { }
            .buffer()
            .flatMap(weatherRepository::saveAll)
    }

    fun canWearTShirtToday(name: String, city: String): Mono<String> {
        if (name.lowercase(Locale.getDefault()) == "akif") {
            return Mono.just("Yes, Akif can wear a t-shirt anywhere, any time.")
        }
        val today = LocalDate.now(clock)
        return queryWeatherInfoByCity(city)
            .filter { it.localDate == today }
            .next()
            .map { weatherInfo ->
                if ((weatherInfo.avgTemperature?.toDouble() ?: 0.0) < T_SHIRT_WEARING_THRESHOLD) {
                    "No, $name cannot wear a t-shirt in $city today."
                } else {
                    "Yes, $name can wear a t-shirt in $city today."
                }
            }
            .switchIfEmpty(Mono.just("Don't know if $name can wear a t-shirt in $city today because weather data is missing"))
    }
}