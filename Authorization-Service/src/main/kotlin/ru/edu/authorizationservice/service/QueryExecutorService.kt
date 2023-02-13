package ru.edu.authorizationservice.service

import feign.FeignException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.feign.queryexecutor.QueryExecutorClient
import ru.edu.authorizationservice.feign.queryexecutor.request.QueryRequest
import ru.edu.authorizationservice.repository.DatabaseRepository
import ru.edu.authorizationservice.repository.UserRepository
import ru.edu.authorizationservice.request.QueryRequestAuth

@Service
class QueryExecutorService(
    private val queryExecutorClient: QueryExecutorClient,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val databaseRepository: DatabaseRepository
) {
    private val logger = KotlinLogging.logger { }
    fun sendQuery(request: QueryRequestAuth, token: String): Any {
        return try {
            val currentUser = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).get()
            val database = databaseRepository.findDatabaseEntityByDatabaseNameAndAndDbmsAndAndUserEntity(
                request.database,
                request.dbms,
                currentUser
            )
            val queryRequest = QueryRequest().apply {
                this.sql = request.sql
                this.database = request.database
                this.dbms = request.dbms
                this.login = database.login.toString()
                this.password = database.passwordDbms.toString()
            }
            logger.debug { "ТУТ ЗАПРОС В QE: ${queryRequest}" }
            queryExecutorClient.sendQuery(queryRequest)
        } catch (ex: FeignException) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.responseBody()), HttpStatus.BAD_REQUEST)
        }
    }
}