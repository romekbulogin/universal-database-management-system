package ru.edu.tablemanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, JpaRepositoriesAutoConfiguration::class, JdbcTemplateAutoConfiguration::class])
class TableManagerApplication

fun main(args: Array<String>) {
    runApplication<TableManagerApplication>(*args)
}
