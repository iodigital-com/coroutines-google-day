package com.io.reactivecoroutines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.blockhound.BlockHound;

import java.time.Clock;

@SpringBootApplication
@EnableR2dbcRepositories
public class ReactiveCoroutinesApplication {
    static {
        BlockHound.builder()
                // allow exception for a Kotlin reflection internal call.
                // .allowBlockingCallsInside("RandomAccessFile", "readBytes")
                .install();
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveCoroutinesApplication.class, args);
    }
}
