package ru.edu.authorizationservice.service

import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
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

    fun registration(request: RegistrationRequest): AuthenticationResponse? {
        try {
            logger.info("[Request] Registration: $request")
            val user = UserEntity().apply {
                setUsername(request.username)
                setEmail(request.email)
                setPassword(passwordEncoder.encode(request.password))
                setIsActivated(false)
                setRole(Role.USER)
            }
            userRepository.save(user)
            val jwtToken = jwtService.generateToken(user)
            return AuthenticationResponse(jwtToken)
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw ex
        }
    }

    fun authentication(request: AuthenticationRequest): AuthenticationResponse? {
        try {
            logger.info("[Request] Authentication: $request")
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
            val user = userRepository.findByEmail(request.email).orElseThrow()
            val jwtToken = jwtService.generateToken(user)
            return AuthenticationResponse(jwtToken)
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw ex
        }
    }
}