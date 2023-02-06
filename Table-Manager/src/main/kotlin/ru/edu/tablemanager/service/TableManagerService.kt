package ru.edu.tablemanager.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.edu.tablemanager.config.dto.Database
import ru.edu.tablemanager.config.dto.DatabaseList
import ru.edu.tablemanager.request.TableViewRequest
import java.sql.DriverManager
import java.sql.SQLException
import java.util.NoSuchElementException

@Service
class TableManagerService(
    private val databaseList: DatabaseList
) {
    private val logger = KotlinLogging.logger { }
    private fun findDriver(driverName: String?): Database? {
        try {
            return databaseList.databases?.stream()?.filter {
                it.driverClassName.equals(driverName)
            }?.findFirst()?.orElseThrow()
        } catch (ex: NoSuchElementException) {
            logger.error(ex.message)
        }
        return null
    }

    fun viewTable(request: TableViewRequest): Any {
        return try {
            val resultQuery = mutableListOf<MutableMap<String, Any?>>()
            var map = mutableMapOf<String, Any?>()
            val database = findDriver(request.dbms)
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