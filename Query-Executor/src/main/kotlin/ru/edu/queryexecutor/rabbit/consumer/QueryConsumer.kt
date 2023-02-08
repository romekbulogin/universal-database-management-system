package ru.edu.queryexecutor.rabbit.consumer

import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.feign.InstancesManagerClient
import ru.edu.queryexecutor.feign.request.InstanceEntity
import ru.edu.queryexecutor.rabbit.producer.QueryProducer
import ru.edu.queryexecutor.request.QueryRequest
import java.sql.SQLException

@Service
class QueryConsumer(
    private val queryProducer: QueryProducer,
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

    @RabbitListener(queues = ["\${spring.rabbitmq.consumer.request.queue}"])
    fun queryHandler(message: Message<String>) {
        logger.info(message.toString())
        val request = QueryRequest().apply {
            sql = message.payload
            database = message.headers["database"].toString()
            dbms = message.headers["dbms"].toString()
        }
        queryProducer.sendQueryResponse(executeQuery(request))
    }

    fun executeQuery(request: QueryRequest): Any? {
        try {
            logger.info("Request execute: $request")
            val dataSource = findDriver(request.dbms)
            val driverManagerDataSources = DriverManagerDataSource().apply {
                url = dataSource?.url + request.database
                username = request.login
                password = request.password
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
            return ex.message
        } catch (ex: NullPointerException) {
            logger.error(ex.message)
            return ex.message
        }
    }
}