package ru.edu.databasemanager.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.edu.databasemanager.config.DatabaseConfiguration
import ru.edu.databasemanager.request.DatabaseRequest
import ru.edu.databasemanager.response.FailedCreatedDatabase
import ru.edu.databasemanager.response.FailedDeletedDatabase
import ru.edu.databasemanager.response.SuccessfullyCreateDatabase
import ru.edu.databasemanager.response.SuccessfullyDeleteDatabase
import java.sql.Connection


@Service
class DatabaseService(
    private val databaseConfiguration: DatabaseConfiguration
) {
    private val logger = KotlinLogging.logger {}

    fun findDriver(driverName: String?): Connection? {
        return databaseConfiguration.dataBaseList.stream().filter { it ->
            driverName.equals(it.dataSource?.connection?.metaData?.databaseProductName)
        }.findAny().get().dataSource?.connection
    }

    fun createDatabase(request: DatabaseRequest): Any {
        return try {
            val connection = findDriver(request.dbms)
            connection?.createStatement()?.executeUpdate("create database ${request.database}")
            logger.info("${connection?.metaData?.databaseProductName}: ${request.database} created successfully")
            SuccessfullyCreateDatabase().apply {
                comment = "Database: ${request.database} created successfully"
                url = "${connection?.metaData?.url}/${request.database}"
            }
        } catch (ex: Exception) {
            logger.error("Database creation error: ${request.database}. Exception: ${ex.message}")
            ResponseEntity(FailedCreatedDatabase().apply {
                error = "Database creation error: ${request.database}"
                exception = ex.message
            }, HttpStatus.BAD_REQUEST)
        }
    }

    fun deleteDatabase(request: DatabaseRequest): Any {
        return try {
            findDriver(request.dbms)?.createStatement()?.executeUpdate("drop database ${request.database}")
            logger.info("Database: ${request.database} deleted successfully")
            SuccessfullyDeleteDatabase(true, "Database: ${request.database} deleted successfully")
        } catch (ex: Exception) {
            logger.error("Database deletion error: ${request.database}. Exception: ${ex.message}")
            ResponseEntity(FailedDeletedDatabase().apply {
                error = "Database deletion error: ${request.database}"
                exception = ex.message
            }, HttpStatus.BAD_REQUEST)
        }
    }

    fun executeQuery(sql: String): Any? {
        return null
    }
}