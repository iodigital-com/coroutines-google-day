package com.io.reactivecoroutines.weather

import com.io.reactivecoroutines.weather.api.WeatherAPIClient
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.asFlux
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
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

    suspend fun getAllWeather(): List<WeatherInfo> {
        log.debug("WeatherService getAllWeather")
        return weatherRepository.findAll(Sort.unsorted()).toList()
    }

    suspend fun getWeatherInfoById(id: Long): WeatherInfo {
        log.debug("WeatherService getWeatherInfoById")
        return weatherRepository.findById(id).first()
    }

    suspend fun queryWeatherInfoByCity(city: String): List<WeatherInfo> {
        log.debug("WeatherService queryWeatherInfoByCity")
        val localDbData = weatherRepository
            .queryWeatherInfoByCity(city).toList().takeIf { it.isNotEmpty() }

        return localDbData ?: fetchAndSaveWeatherInfoByCity(city)
    }

    suspend fun queryByExample(weatherInfo: WeatherInfo): List<WeatherInfo> {
        log.debug("WeatherService queryByExample")
        val locallyStoredWeather = weatherRepository
            .findAll(Example.of(weatherInfo)).asFlow().toList()
            .takeIf { it.isNotEmpty() }

        return locallyStoredWeather
            ?: fetchAndSaveWeatherInfoByExample(weatherInfo)
    }

    suspend fun queryOneByExample(weatherInfo: WeatherInfo): List<WeatherInfo> {
        log.debug("WeatherService querySingleByExample")

        val storedWeather = weatherRepository.findAll(Example.of(weatherInfo)).take(1).asFlow()

        return storedWeather.toList().takeIf { it.isNotEmpty() }
            ?: fetchAndSaveWeatherInfoByExample(weatherInfo)
    }

    suspend fun fetchAndSaveWeatherInfoByCity(city: String?): List<WeatherInfo> {
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

    suspend fun fetchAndSaveWeatherInfoByExample(info: WeatherInfo): List<WeatherInfo> {
        log.debug("WeatherService fetchAndSaveWeatherInfoByExample {}", info)
        return weatherAPI
            .getWeather(info)
            .map { it.toWeatherInfoList() }
            .buffer()
            .asFlux()
            .awaitFirst()
    }

    suspend fun canWearTShirtToday(name: String, city: String): List<String> {
        if (name.lowercase(Locale.getDefault()) == "akif") {
            return listOf("Yes, Akif can wear a t-shirt anywhere, any time.")
        }

        val today = LocalDate.now(clock)

        val result = queryWeatherInfoByCity(city)
            .filter { it.localDate == today }

        if (result.isEmpty()) {
            listOf("Don't know if $name can wear a t-shirt in $city today because weather data is missing")
        }

        return result.map { weatherInfo ->
            if ((weatherInfo.avgTemperature?.toDouble() ?: 0.0) < T_SHIRT_WEARING_THRESHOLD) {
                "No, $name cannot wear a t-shirt in $city today."
            } else {
                "Yes, $name can wear a t-shirt in $city today."
            }
        }
    }
}
