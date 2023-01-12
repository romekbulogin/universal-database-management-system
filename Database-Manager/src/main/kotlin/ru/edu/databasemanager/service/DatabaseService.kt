package ru.edu.databasemanager.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.edu.databasemanager.config.DatabaseConfiguration
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

    fun createDatabase(name: String?, dataBaseName: String?): Any {
        return try {
            val connection = findDriver(dataBaseName)
            connection?.createStatement()?.executeUpdate("create database $name")
            logger.info("${connection?.metaData?.databaseProductName}: $name created successfully")
            SuccessfullyCreateDatabase().apply {
                comment = "Database: $name created successfully"
                url = "${connection?.metaData?.url}/$name"
            }
        } catch (ex: Exception) {
            logger.error("Database creation error: $name. Exception: ${ex.message}")
            ResponseEntity(FailedCreatedDatabase().apply {
                error = "Database creation error: $name"
                exception = ex.message
            }, HttpStatus.BAD_REQUEST)
        }
    }

    fun deleteDatabase(name: String?, dataBaseName: String?): Any {
        return try {
            findDriver(dataBaseName)?.createStatement()?.executeUpdate("drop database $name")
            logger.info("Database: $name deleted successfully")
            SuccessfullyDeleteDatabase(true, "Database: $name deleted successfully")
        } catch (ex: Exception) {
            logger.error("Database deletion error: $name. Exception: ${ex.message}")
            ResponseEntity(FailedDeletedDatabase().apply {
                error = "Database deletion error: $name"
                exception = ex.message
            }, HttpStatus.BAD_REQUEST)
        }
    }

    fun executeQuery(sql: String): Any? {
        return null
    }
}