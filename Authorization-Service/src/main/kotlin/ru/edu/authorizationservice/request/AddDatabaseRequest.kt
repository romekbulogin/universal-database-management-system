package ru.edu.authorizationservice.request

data class AddDatabaseRequest(
    var email: String? = null,
    var database: String? = null,
    var dbms: String? = null
)
