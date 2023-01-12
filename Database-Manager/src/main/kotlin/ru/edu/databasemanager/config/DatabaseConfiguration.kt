package ru.edu.databasemanager.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class DatabaseConfiguration(private val databaseList: DatabaseList) {
    var dataBaseList: MutableList<JdbcTemplate> = mutableListOf()

    @Bean
    fun jdbcTemplateList() {
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