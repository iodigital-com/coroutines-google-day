package com.io.reactivecoroutines.weather

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.Logger
import reactor.util.Loggers
import java.time.Duration
import java.time.LocalDate

private val log: Logger = Loggers.getLogger(WeatherSearchHandler::class.java)

@Component
class WeatherSearchHandler(
    private val weatherService: WeatherService
) {

    fun searchByExample(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(WeatherOptionFilters::class.java)
            .flatMap { filters ->
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
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
                    .doOnNext { log.debug("searchByExample onNext") }
            }
    }

    fun searchOneByExample(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(WeatherOptionFilters::class.java)
            .flatMap { filters ->
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
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
//                        .doOnNext(a -> log.debug("searchOneByExample onNext"))
            }
    }

    fun searchRandomOneByExample(ignoredReq: ServerRequest): Mono<ServerResponse> {
        // bridge some blocking code here with a scheduler
        return Mono.fromCallable(this::getRandomCriteria)
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { randomDateCriteria ->
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        weatherService.queryOneByExample(randomDateCriteria)
                    )
                    .doOnNext { log.debug("searchRandomOneByExample onNext") }
                    .doOnSuccess { log.debug("searchRandomOneByExample doOnSuccess") }
            }
    }

    fun getWeatherStream(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(
            Flux
                .interval(Duration.ofMillis(100))
                .subscribeOn(Schedulers.boundedElastic())
                .map { dayToAdd -> LocalDate.of(1995, 1, 1).plusDays(dayToAdd) }
                .takeUntil { date -> date.isAfter(LocalDate.of(2015, 12, 28)) }
                .map { dateToGet ->
                    WeatherInfo(
                        id = null,
                        region = "Europe",
                        country = "Denmark",
                        state = "",
                        city = "Copenhagen",
                        localDate = dateToGet,
                        avgTemperature = null
                    )
                }
                .flatMap(weatherService::queryOneByExample)
                .log())
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
