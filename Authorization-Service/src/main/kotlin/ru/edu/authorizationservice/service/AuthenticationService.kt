package ru.edu.authorizationservice.service

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.edu.authorizationservice.dto.UserResponse
import ru.edu.authorizationservice.entity.Role
import ru.edu.authorizationservice.entity.UserEntity
import ru.edu.authorizationservice.repository.UserRepository
import ru.edu.authorizationservice.request.AuthenticationRequest
import ru.edu.authorizationservice.request.RegistrationRequest
import ru.edu.authorizationservice.response.AuthenticationResponse

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
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
                }
                userRepository.save(user)
                return AuthenticationResponse(jwtService.generateToken(user), UserResponse().apply {
                    this.username = user.getNickname().toString()
                    this.email = user.getEmail()!!
                    this.password = user.password!!
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
                    this.password = user.password!!
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
            if (token.isNotEmpty() && token != null) {
                logger.info("[Request] Refresh: $token")
                val user = userRepository.findByEmail(jwtService.extractUsername(token.substring(7))).orElseThrow()
                return AuthenticationResponse(jwtService.generateToken(user), UserResponse().apply {
                    this.username = user.getNickname().toString()
                    this.email = user.getEmail()!!
                    this.password = user.password!!
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
}