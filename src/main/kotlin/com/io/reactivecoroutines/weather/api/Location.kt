package com.io.reactivecoroutines.weather.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class Location(
    @JsonProperty("name") val city: String,
    val region: String,
    val country: String,
    @JsonProperty("tz_id") val timezoneId: String,
    @JsonProperty("localtime_epoch") val localTimestamp: Long,
) {
    fun time(): LocalDateTime = LocalDateTime.ofEpochSecond(
        localTimestamp,
        0,
        ZoneId.of(timezoneId).getRules().getOffset(Instant.ofEpochSecond(localTimestamp))
    )
}