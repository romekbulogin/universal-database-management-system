package ru.edu.databasemanager.config

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class DatabaseConfiguration(private val databaseList: DatabaseList) {
    private var logger = KotlinLogging.logger { }
    var dataBaseList: MutableList<JdbcTemplate> = mutableListOf()

    @Bean
    fun jdbcTemplateList() {
        databaseList.databases?.forEach {
            logger.info { it.url }
        }
        databaseList.databases?.forEach {
            dataBaseList.add(JdbcTemplate(DataSourceProperties().apply {
                url = it.url
                username = it.username
                password = it.password
                driverClassName = it.driver
            }.initializeDataSourceBuilder().build()))
        }
    }
}