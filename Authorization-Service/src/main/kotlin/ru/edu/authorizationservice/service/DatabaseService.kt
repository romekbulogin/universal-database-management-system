package ru.edu.authorizationservice.service

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.entity.DatabaseEntity
import ru.edu.authorizationservice.entity.UserEntity
import ru.edu.authorizationservice.repository.DatabaseRepository
import ru.edu.authorizationservice.repository.UserRepository
import ru.edu.authorizationservice.request.AddDatabaseRequest

@Service
class DatabaseService(
    private val databaseRepository: DatabaseRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    private val logger = KotlinLogging.logger { }
    fun addNewDatabaseForUser(request: AddDatabaseRequest, token: String): ResponseEntity<String> {
        return try {
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
            ResponseEntity.ok("database created")
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity.ok(ex.message)
        }
    }
}