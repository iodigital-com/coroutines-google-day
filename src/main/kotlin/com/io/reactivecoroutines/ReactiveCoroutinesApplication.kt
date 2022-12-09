package com.io.reactivecoroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.blockhound.BlockHound

@SpringBootApplication
@EnableR2dbcRepositories
class ReactiveCoroutinesApplication

fun main(args: Array<String>) {
    BlockHound.builder()
        // allow exception for a Kotlin reflection internal call.
        // .allowBlockingCallsInside("RandomAccessFile", "readBytes")
        .install()
    runApplication<ReactiveCoroutinesApplication>(*args)
}
