package ru.edu.instancemanager.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.edu.instancemanager.entity.InstanceEntity
import ru.edu.instancemanager.repository.InstanceRepository

@Service
class InstanceService(private val instanceRepository: InstanceRepository) {
    private val logger = KotlinLogging.logger { }
    fun findInstanceByDbms(dbms: String): ResponseEntity<Any> {
        return try {
            ResponseEntity(instanceRepository.findByDbms(dbms), HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    fun saveInstance(instanceEntity: InstanceEntity): ResponseEntity<Any> {
        return try {
            logger.info("[NEW INSTANCE]: $instanceEntity")
            ResponseEntity(instanceRepository.save(instanceEntity), HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }
}