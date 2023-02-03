package ru.edu.authorizationservice.request

data class DeleteDatabase(
    var username: String? = null,
    var database: String? = null,
    var dbms: String? = null
)
