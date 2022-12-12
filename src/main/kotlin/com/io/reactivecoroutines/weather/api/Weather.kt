package com.io.reactivecoroutines.weather.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Forecast(
    @field:JsonProperty("forecastday") @param:JsonProperty("forecastday") val days: List<ForecastDay>
)

data class ForecastDay(
    val date: LocalDate,
    @JsonProperty("day") val temperature: Temperature,
)

data class Location(
    @JsonProperty("name") val city: String,
    val region: String,
    val country: String,
)

data class Temperature(
    @field:JsonProperty("maxtemp_c") @param:JsonProperty("maxtemp_c") val maxC: Double,
    @field:JsonProperty("maxtemp_f") @param:JsonProperty("maxtemp_f") val maxF: Double,
    @field:JsonProperty("mintemp_c") @param:JsonProperty("mintemp_c") val minC: Double,
    @field:JsonProperty("mintemp_f") @param:JsonProperty("mintemp_f") val minF: Double,
    @field:JsonProperty("avgtemp_c") @param:JsonProperty("avgtemp_c") val avgC: Double,
    @field:JsonProperty("avgtemp_f") @param:JsonProperty("avgtemp_f") val avgF: Double,
)
