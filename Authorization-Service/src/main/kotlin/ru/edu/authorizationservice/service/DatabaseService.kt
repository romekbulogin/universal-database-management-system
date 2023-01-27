package ru.edu.authorizationservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseBody
import ru.edu.authorizationservice.entity.DatabaseEntity
import ru.edu.authorizationservice.entity.UserEntity
import ru.edu.authorizationservice.feign.databasemanager.DatabaseManagerClient
import ru.edu.authorizationservice.feign.databasemanager.response.FailedCreatedDatabase
import ru.edu.authorizationservice.feign.databasemanager.response.SuccessfullyCreateDatabase
import ru.edu.authorizationservice.repository.DatabaseRepository
import ru.edu.authorizationservice.repository.UserRepository
import ru.edu.authorizationservice.request.AddDatabaseRequest

@Service
class DatabaseService(
    private val databaseRepository: DatabaseRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val databaseManagerClient: DatabaseManagerClient
) {
    private val logger = KotlinLogging.logger { }
    private val objectMapper = ObjectMapper()
    fun addNewDatabaseForUser(request: AddDatabaseRequest, token: String): ResponseEntity<Any> {
        return try {
            val response = databaseManagerClient.createDatabase(request)
            logger.debug("Token: $token")
            val currentUser = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).get()
            val database = DatabaseEntity().apply {
                this.dbms = request.dbms
                this.databaseName = request.database
                this.userEntity = currentUser
            }
            databaseRepository.save(database)
            currentUser.addDatabase(database)
            userRepository.save(currentUser)
            ResponseEntity.ok(response)

        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("response" to "Failed to create databases"), HttpStatus.BAD_REQUEST)
        }
    }
}