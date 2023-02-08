package ru.edu.databasemanager.service

import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.edu.databasemanager.feign.InstancesManagerClient
import ru.edu.databasemanager.feign.request.InstanceEntity
import ru.edu.databasemanager.request.CreateUserRequest
import ru.edu.databasemanager.request.DatabaseRequest
import ru.edu.databasemanager.response.*
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*


@Service
class DatabaseService(
    private val instancesManagerClient: InstancesManagerClient
) {
    private val logger = KotlinLogging.logger { }
    fun findDriver(driverName: String): InstanceEntity? {
        return try {
            instancesManagerClient.findInstanceByDbms(driverName)
        } catch (ex: Exception) {
            logger.error(ex.message)
            null
        }
    }

    fun createDatabase(request: DatabaseRequest): ResponseEntity<Any> {
        return try {
            val database = findDriver(request.dbms.toString())
            val user = createUser(
                CreateUserRequest(
                    request.database,
                    request.username,
                    request.dbms
                )
            ).body as CreateUserResponse
            logger.debug(user.toString())
            val connection = DriverManager.getConnection(database?.url, database?.username, database?.password)
            connection?.createStatement()
                ?.executeUpdate("create database ${request.database}; ALTER DATABASE ${request.database} OWNER TO ${user.username};")

            logger.info("${connection?.metaData?.databaseProductName}: ${request.database} created successfully")
            val response = SuccessfullyCreateDatabase().apply {
                url = "${connection?.metaData?.url}${request.database}"
                username = user.username
                password = user.password
            }
            connection!!.endRequest()
            connection.close()
            ResponseEntity(response, HttpStatus.OK)
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
            val database = findDriver(request.dbms.toString())
            val connection = DriverManager.getConnection(database?.url, database?.username, database?.password)
            connection?.createStatement()?.executeUpdate("drop database ${request.database}")
            logger.info("Database: ${request.database} deleted successfully")
            connection?.close()
            ResponseEntity(
                SuccessfullyDeleteDatabase(true, "Database: ${request.database} deleted successfully"),
                HttpStatus.OK
            )
        } catch (ex: Exception) {
            logger.error("Database deletion error: ${request.database}. Exception: ${ex.message}")
            ResponseEntity(FailedDeletedDatabase().apply {
                error = "Database deletion error: ${request.database}"
                exception = ex.message
            }, HttpStatus.BAD_REQUEST)
        }

    }

    private fun createUser(request: CreateUserRequest): ResponseEntity<Any> {
        return try {
            val database = findDriver(request.dbms.toString())
            val connection = DriverManager.getConnection(database?.url, database?.username, database?.password)
            val password = RandomStringUtils.random(30, true, true).lowercase(Locale.getDefault())
            val login = RandomStringUtils.random(10, true, true).lowercase(Locale.getDefault())
            connection.createStatement().execute(
                database?.sqlCreateUser?.replace("usertag", login)
                    ?.replace("passtag", "'${password}'")
            )
            connection.close()
            ResponseEntity(CreateUserResponse(login, password), HttpStatus.OK)
        } catch (ex: SQLException) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

}