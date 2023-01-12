package ru.edu.tablemanager.config.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import ru.edu.tablemanager.config.dto.Database

@Configuration
@ConfigurationProperties(prefix = "unigate")
data class DatabaseList(
    var databases: List<Database>? = null
)
