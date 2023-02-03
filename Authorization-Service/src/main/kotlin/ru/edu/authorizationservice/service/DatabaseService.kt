package ru.edu.authorizationservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException
import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.entity.DatabaseEntity
import ru.edu.authorizationservice.feign.databasemanager.DatabaseManagerClient
import ru.edu.authorizationservice.feign.databasemanager.response.createddatabase.FailedCreatedDatabase
import ru.edu.authorizationservice.feign.databasemanager.response.deleteddatabase.FailedDeletedDatabase
import ru.edu.authorizationservice.repository.DatabaseRepository
import ru.edu.authorizationservice.repository.UserRepository
import ru.edu.authorizationservice.request.AddDatabaseRequest
import ru.edu.authorizationservice.request.DeleteDatabase

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
            val passwordEncoder = BCryptPasswordEncoder()
            val response = databaseManagerClient.createDatabase(request)
            val currentUser = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).get()
            val database = DatabaseEntity().apply {
                this.dbms = request.dbms
                this.databaseName = request.database
                this.userEntity = currentUser
                this.login = response.username
                this.passwordDbms = passwordEncoder.encode(response.password)
            }
            databaseRepository.save(database)
            currentUser.addDatabase(database)
            userRepository.save(currentUser)
            ResponseEntity.ok(response)

        } catch (ex: FeignException) {
            logger.error(ex.message)
            ResponseEntity(
                objectMapper.readValue(ex.content(), FailedCreatedDatabase::class.java),
                HttpStatus.BAD_REQUEST
            )
        }
    }

    fun deleteDeleteDatabase(request: DeleteDatabase): ResponseEntity<Any> {
        return try {
            logger.info("[Delete] $request")
            val user = userRepository.findByUsername(request.username!!)
            val database = databaseRepository.findDatabaseEntityByDatabaseNameAndAndDbmsAndAndUserEntity(
                request.database!!,
                request.dbms!!,
                user
            )
            user.deleteDatabase(database)
            databaseRepository.delete(database)
            ResponseEntity(databaseManagerClient.deleteDatabase(request), HttpStatus.OK)
        } catch (ex: FeignException) {
            logger.error(ex.message)
            ResponseEntity(
                objectMapper.readValue(ex.content(), FailedDeletedDatabase::class.java),
                HttpStatus.BAD_REQUEST
            )
        } catch (ex: EmptyResultDataAccessException) {
            logger.error(ex.message)
            ResponseEntity(mapOf("response" to "Database ${request.database} is not exist"), HttpStatus.BAD_REQUEST)
        }
    }
}