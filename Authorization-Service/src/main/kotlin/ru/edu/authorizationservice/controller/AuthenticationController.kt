package ru.edu.authorizationservice.controller

import org.springframework.web.bind.annotation.*
import ru.edu.authorizationservice.request.AuthenticationRequest
import ru.edu.authorizationservice.request.RegistrationRequest
import ru.edu.authorizationservice.service.AuthenticationService

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/registration")
    fun registration(@RequestBody request: RegistrationRequest) = authenticationService.registration(request)

    @PostMapping("/authentication")
    fun authentication(@RequestBody request: AuthenticationRequest) = authenticationService.authentication(request)


    @GetMapping("/refresh")
    fun refresh(@RequestHeader(value = "Authorization") token: String) = authenticationService.refresh(token)

    @GetMapping("/verify/{uuid}")
    fun verify(@PathVariable(name = "uuid") uuid: String) = authenticationService.verify(uuid)
}