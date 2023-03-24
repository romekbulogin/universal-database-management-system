package ru.edu.tablemanager.service

import mu.KotlinLogging
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Service
import ru.edu.tablemanager.feign.authentication.AuthenticationServiceClient
import ru.edu.tablemanager.feign.authentication.request.AuthParamRequest
import ru.edu.tablemanager.feign.instances.InstancesManagerClient
import ru.edu.tablemanager.feign.instances.request.InstanceEntity
import ru.edu.tablemanager.request.TableCreateRequest
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

    fun createTable(request: TableCreateRequest) {
        try {
            //        val response = authenticationServiceClient.getAuthParam(AuthParamRequest(request.database, request.dbms))
//        val database = findDriver(request.dbms.toString())
//        val connection =
//            DriverManager.getConnection("${database?.url}${request.database}", response.login, response.password)
            val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/client", "postgres", "1337")
//        val dslContext = DSL.using(connection, SQLDialect.valueOf(request.dbms.toString()))
            val dslContext = DSL.using(connection, SQLDialect.POSTGRES)
            logger.debug { request }
            val createTable = dslContext.createTable(request.tableName)
            createTable.column(
                request.primaryKey?.columnName,
                SQLEnum.getSqlDataType(request.primaryKey?.dataType!!)?.identity(request.primaryKey?.isIdentity!!)
                    ?.length(request.primaryKey?.length!!)
            ).constraints(
                constraint(request.primaryKey?.name).primaryKey(request.primaryKey?.columnName)
            )
            request.rawTable.forEach {
                createTable.column(
                    it.name,
                    SQLEnum.getSqlDataType(it.dataType!!)?.sqlDataType?.nullable(it.isNull!!)?.length(it.length)
                )
            }
            request.uniqueAttributes?.forEach {
                createTable.unique(it)
            }
            createTable.execute()
            //ебаный костыль, но хуле поделать
            request.defaultValues?.forEach {
                dslContext.alterTable(request.tableName).alterColumn(it.key).defaultValue(it.value).execute()
            }
        } catch (ex: Exception) {
            logger.error(ex.message)
        }
    }
}

