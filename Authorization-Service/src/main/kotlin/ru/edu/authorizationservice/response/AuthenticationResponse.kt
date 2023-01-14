package ru.edu.authorizationservice.response

import ru.edu.authorizationservice.dto.UserResponse

data class AuthenticationResponse(
    var token: String? = null,
    var user: UserResponse? = null
)