package ru.edu.authorizationservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.Serializers.Base
import feign.FeignException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.entity.DatabaseEntity
import ru.edu.authorizationservice.feign.databasemanager.DatabaseManagerClient
import ru.edu.authorizationservice.feign.databasemanager.response.createddatabase.FailedCreatedDatabase
import ru.edu.authorizationservice.feign.databasemanager.response.deleteddatabase.FailedDeletedDatabase
import ru.edu.authorizationservice.repository.DatabaseRepository
import ru.edu.authorizationservice.repository.UserRepository
import ru.edu.authorizationservice.request.AddDatabaseRequest
import ru.edu.authorizationservice.request.DeleteDatabase
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


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
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey())
            val response = databaseManagerClient.createDatabase(request)
            val currentUser = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).get()
            val database = DatabaseEntity().apply {
                this.dbms = request.dbms
                this.databaseName = request.database
                this.userEntity = currentUser
                this.login = response.username
                this.passwordDbms = Base64.getEncoder().encodeToString(cipher.doFinal(response.password?.toByteArray(Charsets.UTF_8)))
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

    fun deleteDeleteDatabase(request: DeleteDatabase, token: String): ResponseEntity<Any> {
        return try {
            logger.info("[Delete] $request")
            val user = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).get()
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

    fun viewDatabaseList(token: String): ResponseEntity<Any> {
        return try {
            val currentUser = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).get()
            val databases = databaseRepository.findAllByUserEntity(currentUser)
            ResponseEntity(databases, HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    private fun getPublicKey(): PublicKey? {
        val key = String(
            Files.readAllBytes(Paths.get("Authorization-Service\\src\\main\\resources\\publickey.pem")),
            Charset.defaultCharset()
        )

        val publicKeyPEM = key
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace(System.lineSeparator().toRegex(), "")
            .replace("-----END PUBLIC KEY-----", "")

        val encoded: ByteArray = Base64.getDecoder().decode(publicKeyPEM)

        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(encoded)
        return keyFactory.generatePublic(keySpec) as PublicKey
    }
}