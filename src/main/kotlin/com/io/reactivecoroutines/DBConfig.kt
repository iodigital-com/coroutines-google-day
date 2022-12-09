package com.io.reactivecoroutines

import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class DBConfig(
    @Value("\${com.io.reload-db-data}") private val insertData: Boolean
) {

    @Bean
    fun initializer(connectionFactory: ConnectionFactory) = ConnectionFactoryInitializer().apply {
        setConnectionFactory(connectionFactory)
        if (insertData) {
            setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("data.sql")))
        }
    }
}
