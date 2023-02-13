package ru.edu.tablemanager.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.edu.tablemanager.feign.authentication.AuthenticationServiceClient
import ru.edu.tablemanager.feign.authentication.request.AuthParamRequest
import ru.edu.tablemanager.feign.instances.InstancesManagerClient
import ru.edu.tablemanager.feign.instances.request.InstanceEntity
import ru.edu.tablemanager.request.TableViewRequest
import java.sql.DriverManager
import java.sql.SQLException

@Service
class TableManagerService(
    private val instancesManagerClient: InstancesManagerClient,
    private val authenticationServiceClient: AuthenticationServiceClient
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
            val response = authenticationServiceClient.getAuthParam(AuthParamRequest(request.database, request.dbms))
            val resultQuery = mutableListOf<MutableMap<String, Any?>>()
            var map = mutableMapOf<String, Any?>()
            val database = findDriver(request.dbms.toString())
            val connection =
                DriverManager.getConnection("${database?.url}${request.database}", response.login, response.password)
            val resultSet = connection.createStatement().executeQuery("select * from ${request.table} limit 50")


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

/*
create table public.auth
(
    id  serial  primary key,
    firstname varchar not null,
    lastname  varchar not null,
    midname   varchar not null
);
*/