package ru.edu.authorizationservice.dto

import ru.edu.authorizationservice.entity.Role


data class UserResponse(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var isActivated: Boolean? = null,
    var role: Role? = null
)
