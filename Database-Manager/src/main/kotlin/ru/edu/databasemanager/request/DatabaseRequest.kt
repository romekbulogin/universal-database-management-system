package ru.edu.databasemanager.request

data class DatabaseRequest(
    var username: String? = null,
    var database: String? = null,
    var dbms: String? = null
)
