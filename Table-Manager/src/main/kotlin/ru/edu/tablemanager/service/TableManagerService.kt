package ru.edu.tablemanager.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.edu.tablemanager.feign.InstancesManagerClient
import ru.edu.tablemanager.feign.request.InstanceEntity
import ru.edu.tablemanager.request.TableViewRequest
import java.sql.DriverManager
import java.sql.SQLException
import java.util.NoSuchElementException

@Service
class TableManagerService(
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

    fun viewTable(request: TableViewRequest): Any {
        return try {
            val resultQuery = mutableListOf<MutableMap<String, Any?>>()
            var map = mutableMapOf<String, Any?>()
            val database = findDriver(request.dbms.toString())
            val connection =
                DriverManager.getConnection("${database?.url}${request.database}", request.login, request.password)
            val resultSet = connection.createStatement().executeQuery("select * from ${request.table}")


            while (resultSet.next()) {
                for (i in 1..resultSet.metaData.columnCount) {
                    map[resultSet.metaData.getColumnName(i)] = resultSet.getObject(i)
                }
                resultQuery.add(map)
                map = mutableMapOf()
            }
            resultQuery
        } catch (ex: SQLException) {
            logger.error(ex.message)
            mapOf("error" to ex.message)
        }
    }
}