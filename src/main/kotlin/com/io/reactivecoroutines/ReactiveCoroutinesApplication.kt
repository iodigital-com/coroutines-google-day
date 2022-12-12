package com.io.reactivecoroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration

@SpringBootApplication
@EnableR2dbcRepositories
class ReactiveCoroutinesApplication {
    init {
        BlockHound.install(
            // WebFlux internal
            BlockHoundIntegration { builder ->
                builder.allowBlockingCallsInside(
                    "java.util.ServiceLoader\$LazyClassPathLookupIterator",
                    "parse"
                )
            },
            // Kotlin reflection
            BlockHoundIntegration { builder ->
                builder.allowBlockingCallsInside(
                    "kotlin.reflect.jvm.ReflectJvmMapping",
                    "getKotlinFunction"
                )
            },
        )
    }
}

fun main(args: Array<String>) {
    runApplication<ReactiveCoroutinesApplication>(*args)
}
