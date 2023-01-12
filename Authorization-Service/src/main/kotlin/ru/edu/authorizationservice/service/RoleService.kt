package ru.edu.authorizationservice.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.entity.RoleEntity
import ru.edu.authorizationservice.repository.RoleRepository

@Service
class RoleService(private val roleRepository: RoleRepository) {

    private val logger = KotlinLogging.logger { }
    fun saveRole(roleEntity: RoleEntity): ResponseEntity<Any> {
        return try {
            roleRepository.save(roleEntity)
            ResponseEntity(HttpStatus.CREATED)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    fun findRoles(): ResponseEntity<Any> {
        return try {
            ResponseEntity(roleRepository.findAll(), HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.NOT_FOUND)
        }
    }

    fun findByName(name: String): ResponseEntity<Any> {
        return try {
            ResponseEntity(roleRepository.findByName(name), HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.NOT_FOUND)
        }
    }
}