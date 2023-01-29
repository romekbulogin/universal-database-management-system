package ru.edu.authorizationservice.feign.databasemanager.response.createduser

data class CreateUserResponse(
    var username: String? = null,
    var password: String? = null
)