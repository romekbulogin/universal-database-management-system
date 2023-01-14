package ru.edu.queryexecutor.service

import mu.KotlinLogging
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.config.dto.Database
import ru.edu.queryexecutor.config.dto.DatabaseList
import ru.edu.queryexecutor.request.FindDatabases
import java.sql.SQLException

@Service
class DatabaseListViewService(private val databaseList: DatabaseList) {
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

    fun findAllDatabasesForUser(request: FindDatabases): Any? {
        try {
            val dataSource = findDriver(request.dbms)
            val driverManagerDataSources = DriverManagerDataSource().apply {
                url = dataSource?.url
                username = dataSource?.username
                password = dataSource?.password
            }
            val resultSet = driverManagerDataSources.connection.createStatement()
                ?.executeQuery("select rolname as username,datname as database from pg_roles  full outer join pg_database pa on pg_roles.oid = pa.datdba where rolname = '${request.username}'")

            val result = mutableListOf<MutableMap<String, Any?>>()
            var map = mutableMapOf<String, Any?>()

            while (resultSet?.next() == true) {
                for (i in 2..resultSet.metaData.columnCount) {
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