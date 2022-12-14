package ru.edu.databasemanager.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class DatabaseConfig(private val databaseConfig: DatabaseList) {
    var dataBaseList: MutableList<JdbcTemplate> = mutableListOf()

    @Bean
    fun jdbcTemplateList() {
        databaseConfig.databases?.forEach {
            dataBaseList.add(JdbcTemplate(DataSourceProperties().apply {
                url = it.url
                username = it.userName
                password = it.password
                driverClassName = it.driver
            }.initializeDataSourceBuilder().build()))
        }
    }
}