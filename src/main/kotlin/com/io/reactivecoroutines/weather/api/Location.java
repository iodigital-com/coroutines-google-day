package com.io.reactivecoroutines.weather.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record Location(
        @JsonProperty("name") String city,
        String region,
        String country,
        @JsonProperty("tz_id") String timezoneId,
        @JsonProperty("localtime_epoch") long localTimestamp
) {
    public LocalDateTime time() {
        return LocalDateTime.ofEpochSecond(localTimestamp, 0, ZoneId.of(timezoneId).getRules().getOffset(Instant.ofEpochSecond(localTimestamp)));
    }
}