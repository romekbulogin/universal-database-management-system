package ru.edu.authorizationservice.entity

import jakarta.persistence.*

data class DatabaseEntityWithoutUser(
    var dbms: String? = null,
    var databaseName: String? = null,
    var login: String? = null,
    var passwordDbms: String? = null,
)