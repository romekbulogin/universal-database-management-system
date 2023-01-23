package ru.edu.authorizationservice.request

data class AuthenticationRequest(
    var email: String,
    var password: String
)
