package ru.edu.queryexecutor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, JpaRepositoriesAutoConfiguration::class, JdbcTemplateAutoConfiguration::class])
@EnableFeignClients
class QueryExecutorApplication

fun main(args: Array<String>) {
    runApplication<QueryExecutorApplication>(*args)
}