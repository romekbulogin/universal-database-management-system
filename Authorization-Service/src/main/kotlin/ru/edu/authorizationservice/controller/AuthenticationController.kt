package ru.edu.authorizationservice.controller

import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.authorizationservice.request.AuthenticationRequest
import ru.edu.authorizationservice.request.RegistrationRequest
import ru.edu.authorizationservice.response.AuthenticationResponse
import ru.edu.authorizationservice.service.AuthenticationService

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/registration")
    fun registration(@RequestBody request: RegistrationRequest): ResponseEntity<AuthenticationResponse> =
        ResponseEntity.ok(authenticationService.registration(request))

    @PostMapping("/authentication")
    fun authentication(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> =
        ResponseEntity.ok(authenticationService.authentication(request))
}