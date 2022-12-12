package com.io.reactivecoroutines

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class ClockConfig {

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}
