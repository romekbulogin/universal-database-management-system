package ru.edu.authorizationservice.feign.databasemanager.request

data class CreateUserRequest(
    var username: String? = null,
    var dbms: String? = null
)
