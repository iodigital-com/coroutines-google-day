package com.io.reactivecoroutines.weather

import org.springframework.data.annotation.Id
import java.io.Serial
import java.io.Serializable
import java.time.LocalDate

data class WeatherInfo(
    @Id
    val id: Long? = null,
    val region: String?,
    val country: String?,
    val state: String?,
    val city: String?,
    val localDate: LocalDate?,
    val avgTemperature: String?,
) : Serializable {
    companion object {
        @Serial
        val serialVersionUID: Long = 0L
    }
}
