package ru.edu.querycreator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class QueryCreatorApplication

fun main(args: Array<String>) {
    runApplication<QueryCreatorApplication>(*args)
}
