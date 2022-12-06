package com.io.reactivecoroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.blockhound.BlockHound

@SpringBootApplication
@EnableR2dbcRepositories
class ReactiveCoroutinesApplication

fun main(args: Array<String>) {
	BlockHound.install()
	runApplication<ReactiveCoroutinesApplication>(*args)
}
