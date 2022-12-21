package ru.edu.authorizationservice.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.entity.RoleEntity
import ru.edu.authorizationservice.entity.UsersEntity
import ru.edu.authorizationservice.repository.RoleRepository
import ru.edu.authorizationservice.repository.UserRepository
import kotlin.math.log

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
) :
    UserDetailsService {
    private val logger = KotlinLogging.logger { }
    fun saveUser(usersEntity: UsersEntity): ResponseEntity<Any> {
        return try {
            usersEntity.password = BCryptPasswordEncoder().encode(usersEntity.password)
            userRepository.save(usersEntity)
            ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    fun saveRole(roleEntity: RoleEntity): ResponseEntity<Any> {
        return try {
            roleRepository.save(roleEntity)
            ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    fun addRoleToUser(username: String, role: String): ResponseEntity<Any> {
        return try {
            val user = userRepository.findByUsername(username)
            val role = roleRepository.findByName(role)
            ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    fun getUser(username: String): ResponseEntity<Any> {
        return try {
            val user = userRepository.findByUsername(username)
            ResponseEntity(user, HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    fun getUsers(): ResponseEntity<Any> {
        return try {
            val user = userRepository.findAll()
            ResponseEntity(user, HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }

    override fun loadUserByUsername(username: String?): UserDetails? {
        val user = userRepository.findByUsername(username)
        if (user == null) {
            logger.error("User $username not found")
            throw UsernameNotFoundException("User $username not found")
        } else {
            logger.info("User $username found success")
        }
        val authorities = mutableListOf<SimpleGrantedAuthority>()
        user.roles.forEach {
            authorities.add(SimpleGrantedAuthority(it.name))
        }
        return User(user.username, user.password, authorities)
    }
}