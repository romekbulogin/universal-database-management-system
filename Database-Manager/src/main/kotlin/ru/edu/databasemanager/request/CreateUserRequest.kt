package ru.edu.databasemanager.request

data class CreateUserRequest(
    var database: String? = null,
    var username: String? = null,
    var dbms: String? = null
)
