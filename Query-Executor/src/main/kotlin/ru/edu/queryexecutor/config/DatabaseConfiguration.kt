package ru.edu.queryexecutor.config

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import ru.edu.queryexecutor.config.dto.DatabaseList

@Configuration
class DatabaseConfiguration(private val databaseList: DatabaseList) {
    private val logger = KotlinLogging.logger { }
    private var driverManagerDataSources: MutableList<DriverManagerDataSource> = mutableListOf()

    @Bean
    fun initDatabase() {
        databaseList.databases?.forEach {
            driverManagerDataSources.add(DriverManagerDataSource().apply {
                url = it.url
                username = it.username
                password = it.password
            })
            logger.info("Connect to ${it.url}")
        }
    }
}
