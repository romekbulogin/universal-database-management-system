package ru.edu.authorizationservice.request

data class AddDatabaseRequest(
    var database: String? = null,
    var dbms: String? = null
)
