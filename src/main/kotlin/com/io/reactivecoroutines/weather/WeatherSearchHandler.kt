package com.io.reactivecoroutines.weather

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyToFlow
import reactor.util.Logger
import reactor.util.Loggers
import java.time.LocalDate

private val log: Logger = Loggers.getLogger(WeatherSearchHandler::class.java)

@Component
class WeatherSearchHandler(
    private val weatherService: WeatherService
) {

    suspend fun searchByExample(req: ServerRequest): ServerResponse {
        val filters = req.bodyToFlow<WeatherOptionFilters>().first()

        return ServerResponse.ok().bodyAndAwait(
            weatherService.queryByExample(
                WeatherInfo(
                    id = null,
                    region = filters.region,
                    country = filters.country,
                    state = filters.state,
                    city = filters.city,
                    localDate = filters.localDate,
                    avgTemperature = filters.avgTemperature
                )
            )
        )
    }

    suspend fun searchOneByExample(req: ServerRequest): ServerResponse {
        val filters = req.bodyToFlow<WeatherOptionFilters>().first()

        return ServerResponse.ok().bodyAndAwait(
            weatherService.queryOneByExample(
                WeatherInfo(
                    id = null,
                    region = filters.region,
                    country = filters.country,
                    state = filters.state,
                    city = filters.city,
                    localDate = filters.localDate,
                    avgTemperature = filters.avgTemperature
                )
            )
        )
    }

    suspend fun searchRandomOneByExample(ignoredReq: ServerRequest): ServerResponse {
        val serverResponse = ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(weatherService.queryOneByExample(getRandomCriteria()))

        log.debug("searchRandomOneByExample doOnSuccess")

        return serverResponse
    }

    suspend fun getWeatherStream(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(
            (1..7666L)
                .asFlow()
                .onEach { delay(100) }
                .flatMapConcat { daysToAdd ->
                    weatherService.queryOneByExample(
                        WeatherInfo(
                            id = null,
                            region = "Europe",
                            country = "Denmark",
                            state = "",
                            city = "Copenhagen",
                            localDate = LocalDate.of(1995, 1, 1).plusDays(daysToAdd),
                            avgTemperature = null
                        )
                    )
                }
        )
    }

    private fun getRandomCriteria(): WeatherInfo {
        // simulate a blocking implementation
        try {
            log.debug("getRandomCriteria")
            Thread.sleep(10)
            log.debug("getRandomCriteria after sleep")
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

        val randomDayOfMonth: Int = (1..30).random()

        return WeatherInfo(
            region = "Europe",
            country = "Finland",
            state = "",
            city = "Helsinki",
            localDate = LocalDate.of(2019, 7, randomDayOfMonth),
            avgTemperature = null
        )
    }

    private data class WeatherOptionFilters(
        val region: String?,
        val country: String?,
        val state: String?,
        val city: String?,
        val localDate: LocalDate?,
        val avgTemperature: String?
    )
}
