package ru.edu.databasemanager.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.databasemanager.request.DatabaseRequest
import ru.edu.databasemanager.service.DatabaseService

@RestController
@RequestMapping("/api")
class DatabaseController(
    private val dataBaseService: DatabaseService,
) {
    private val logger = KotlinLogging.logger { }

    @PostMapping("/create_database")
    fun createDataBase(@RequestBody request: DatabaseRequest): Any {
        logger.info("Request for create database: $request")
        return dataBaseService.createDatabase(request.name, request.dataBaseName)
    }

    @PostMapping("/delete_database")
    fun deleteDataBase(@RequestBody request: DatabaseRequest): Any {
        logger.info("Request for delete database: $request")
        return dataBaseService.deleteDatabase(request.name, request.dataBaseName)
    }

    @PostMapping("/execute")
    fun executeQuery(@RequestBody request: DatabaseRequest): Any? {
        logger.info("Request for execute query: $request")
        return dataBaseService.executeQuery(request.name.toString())
    }
}