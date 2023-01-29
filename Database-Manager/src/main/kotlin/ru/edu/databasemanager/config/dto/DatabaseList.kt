package ru.edu.databasemanager.config.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "unigate")
data class DatabaseList(
    var databases: List<Database>? = null
)
