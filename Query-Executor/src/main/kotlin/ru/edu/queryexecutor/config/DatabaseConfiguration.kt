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
    fun initDatabase(): MutableList<DriverManagerDataSource>? {
        try {
            databaseList.databases?.forEach {
                val dataSource = DriverManagerDataSource().apply {
                    url = it.url
                    username = it.username
                    password = it.password
                }
                if (!dataSource.connection.isClosed) {
                    driverManagerDataSources.add(dataSource)
                    logger.info("Connect to: ${dataSource.url}")
                }
            }
            return driverManagerDataSources
        } catch (ex: Exception) {
            logger.error(ex.message)
        }
        return driverManagerDataSources
    }
}
