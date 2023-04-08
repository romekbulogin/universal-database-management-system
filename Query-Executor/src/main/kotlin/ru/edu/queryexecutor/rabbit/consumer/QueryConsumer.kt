package ru.edu.queryexecutor.rabbit.consumer

import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.dto.UserCredentials
import ru.edu.queryexecutor.feign.InstancesManagerClient
import ru.edu.queryexecutor.feign.request.InstanceEntity
import ru.edu.queryexecutor.request.QueryRequest
import java.sql.SQLException
import java.util.*
import javax.crypto.Cipher

@Service
class QueryConsumer(
    private val instancesManagerClient: InstancesManagerClient,
    private val mainDatabaseInstance: DriverManagerDataSource,
    private val decryptCipher: Cipher
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

    @RabbitListener(queues = ["\${spring.rabbitmq.consumer.request.queue}"], returnExceptions = "true")
    fun queryHandler(message: Message<String>): Any? {
        return try {
            logger.info(message.toString())
            val request = QueryRequest().apply {
                sql = message.payload
                database = message.headers["database"].toString()
                dbms = message.headers["dbms"].toString()
                login = message.headers["login"].toString()
            }
            return executeQuery(request)
        } catch (ex: Exception) {
            logger.error(ex.message)
            mapOf("error" to ex.message.toString())
        }
    }

    fun executeQuery(request: QueryRequest): Any? {
        try {
            logger.info("Request execute: $request")
            val dataSource = findDriver(request.dbms)
            val userCredentials = getUserCredentials(request.login, request.database)
            val driverManagerDataSources = DriverManagerDataSource().apply {
                url = dataSource?.url + request.database
                username = userCredentials?.login
                password = String(
                    decryptCipher.doFinal(
                        Base64.getDecoder().decode(userCredentials?.password)
                    )
                )
            }
            logger.debug("Current connection: ${dataSource?.url}")
            val resultSet = driverManagerDataSources.connection.createStatement()?.executeQuery(request.sql)

            val result = mutableListOf<MutableMap<String, Any?>>()
            var map = mutableMapOf<String, Any?>()

            while (resultSet?.next() == true) {
                for (i in 1..resultSet.metaData.columnCount) {
                    map[resultSet.metaData.getColumnName(i)] = resultSet.getObject(i)
                }
                result.add(map)
                map = mutableMapOf()
            }
            driverManagerDataSources.connection.close()
            return result
        } catch (ex: SQLException) {
            logger.error(ex.message)
            throw ex
        }
    }

    private fun getUserCredentials(username: String, database: String): UserCredentials? {
        return try {
            val userCredentials = UserCredentials()
            val statement =
                mainDatabaseInstance.connection.prepareStatement("select login,password_dbms from _databases inner join _user u on u.id = _databases.user_entity_id where username = ? and database_name = ?")
            statement.setString(1, username)
            statement.setString(2, database)
            val resultSet = statement.executeQuery()
            while (resultSet?.next() == true) {
                for (i in 1..resultSet.metaData.columnCount) {
                    userCredentials.apply {
                        login = resultSet.getString("login")
                        password = resultSet.getString("password_dbms")
                    }
                }
            }
            userCredentials
        } catch (ex: Exception) {
            logger.error(ex.message)
            null
        }
    }
}