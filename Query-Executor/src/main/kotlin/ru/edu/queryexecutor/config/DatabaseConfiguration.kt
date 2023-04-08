package ru.edu.queryexecutor.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource

@Configuration
class DatabaseConfiguration {

    @Value("\${database.url}")
    private val urlDatabase: String? = null

    @Value("\${database.username}")
    private val usernameDatabase: String? = null

    @Value("\${database.password}")
    private val passwordDatabase: String? = null

    @Bean
    fun mainDatabaseInstance() = DriverManagerDataSource().apply {
        url = urlDatabase
        username = usernameDatabase
        password = passwordDatabase
    }
}