package ru.edu.databasemanager.service

import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.apache.logging.log4j.core.util.PasswordDecryptor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.stereotype.Service
import ru.edu.databasemanager.config.dto.Database
import ru.edu.databasemanager.config.dto.DatabaseList
import ru.edu.databasemanager.request.CreateUserRequest
import ru.edu.databasemanager.request.DatabaseRequest
import ru.edu.databasemanager.response.*
import java.security.MessageDigest
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*


@Service
class DatabaseService(
    private val databaseList: DatabaseList
) {
    private val logger = KotlinLogging.logger { }
    fun findDriver(driverName: String?): Database? {
        try {
            return databaseList.databases?.stream()?.filter {
                it.driverClassName.equals(driverName)
            }?.findFirst()?.orElseThrow()
        } catch (ex: NoSuchElementException) {
            logger.error(ex.message)
        }
        return null
    }

    fun createDatabase(request: DatabaseRequest): ResponseEntity<Any> {
        return try {
            val database = findDriver(request.dbms)
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
                ?.executeUpdate("create database ${request.database}; ALTER DATABASE ${request.database} OWNER TO ${request.username};")

            logger.info("${connection?.metaData?.databaseProductName}: ${request.database} created successfully")
            val response = SuccessfullyCreateDatabase().apply {
                comment = "Database: ${request.database} created successfully"
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
            val database = findDriver(request.dbms)
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
            val database = findDriver(request.dbms)
            val connection = DriverManager.getConnection(database?.url, database?.username, database?.password)
            var password: String? = null
            val connectionToMainDatabase =
                DriverManager.getConnection("jdbc:postgresql://localhost:5432/udbms_users", "postgres", "1337")
            val resultSet = connectionToMainDatabase.createStatement()
                .executeQuery("select password from _user where username = '${request.username}'")
            if (resultSet.next()) {
                password = resultSet.getString("password")
            }

            connection.createStatement().execute(
                database?.sqlCreateUser?.replace("usertag", request.username!!)?.replace("passtag", "'${password}'")
            )
            connection.close()
            ResponseEntity(CreateUserResponse(request.username, password), HttpStatus.OK)
        } catch (ex: SQLException) {
            logger.error(ex.message)
            var password: String? = null
            val connection =
                DriverManager.getConnection("jdbc:postgresql://localhost:5432/udbms_users", "postgres", "1337")
            val resultSet = connection.createStatement()
                .executeQuery("select password from _user where username = '${request.username}'")
            if (resultSet.next()) {
                password = resultSet.getString("password")
            }
            ResponseEntity(CreateUserResponse(request.username, password), HttpStatus.BAD_REQUEST)
        }
    }

}