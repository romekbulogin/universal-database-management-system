package ru.edu.authorizationservice.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.dto.UserResponse
import ru.edu.authorizationservice.entity.Role
import ru.edu.authorizationservice.entity.UserEntity
import ru.edu.authorizationservice.feign.databasemanager.DatabaseManagerClient
import ru.edu.authorizationservice.repository.UserRepository
import ru.edu.authorizationservice.request.AuthenticationRequest
import ru.edu.authorizationservice.request.RegistrationRequest
import ru.edu.authorizationservice.response.AuthenticationResponse
import java.sql.DriverManager
import java.util.UUID

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val mailService: MailService
) {
    private val logger = KotlinLogging.logger { }

    fun registration(request: RegistrationRequest): Any {
        try {
            if (request.email.isNotEmpty() && request.username.isNotEmpty() && request.password.isNotEmpty()) {
                logger.info("[Request] Registration: $request")
                val user = UserEntity().apply {
                    setUsername(request.username)
                    setEmail(request.email)
                    setPassword(passwordEncoder.encode(request.password))
                    setIsActivated(false)
                    setRole(Role.INACTIVE_USER)
                    setActivatedUUID(UUID.randomUUID().toString())
                }
                userRepository.save(user)
                mailService.sendMessageVerify(
                    request.email,
                    "Verify your email",
                    "http://localhost:8080/api/auth/verify/${user.getActivatedUUID()}"
                )
                return AuthenticationResponse(jwtService.generateToken(user), UserResponse().apply {
                    this.username = user.getNickname().toString()
                    this.email = user.getEmail()!!
                    this.isActivated = user.getIsActivated()!!
                    this.role = user.getRole()
                })
            } else {
                return ResponseEntity.badRequest()
            }
        } catch (ex: Exception) {
            logger.error(ex.message)
            return ResponseEntity.badRequest()
        }
    }

    fun authentication(request: AuthenticationRequest): Any {
        try {
            if (request.email.isNotEmpty() && request.password.isNotEmpty()) {
                logger.info("[Request] Authentication: $request")
                authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
                val user = userRepository.findByEmail(request.email).orElseThrow()
                return AuthenticationResponse(jwtService.generateToken(user), UserResponse().apply {
                    this.username = user.getNickname().toString()
                    this.email = user.getEmail()!!
                    this.isActivated = user.getIsActivated()!!
                    this.role = user.getRole()
                })
            } else {
                return ResponseEntity.badRequest()
            }
        } catch (ex: Exception) {
            logger.error(ex.message)
            return ResponseEntity.badRequest()
        }
    }

    fun refresh(token: String): Any {
        try {
            return if (token.isNotEmpty() && token != null) {
                logger.info("[Request] Refresh: $token")
                val user = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).orElseThrow()
                AuthenticationResponse(jwtService.generateToken(user), UserResponse().apply {
                    this.username = user.getNickname().toString()
                    this.email = user.getEmail()!!
                    this.isActivated = user.getIsActivated()!!
                    this.role = user.getRole()
                })
            } else {
                ResponseEntity.badRequest()
            }
        } catch (ex: Exception) {
            logger.error(ex.message)
            return ResponseEntity.badRequest()
        }
    }

    fun verify(uuid: String): ResponseEntity<Map<String, String>> {
        return try {
            logger.debug("Verify by UUID: $uuid")
            val user = userRepository.findByActivatedUUID(uuid)
            user.setIsActivated(true)
            userRepository.save(user)
            ResponseEntity(mapOf("verify" to "success"), HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("verify" to "failed"), HttpStatus.BAD_REQUEST)
        }
    }
}