package ru.edu.databasemanager.config

data class Database(
    var url: String? = null,
    var username: String? = null,
    var password: String? = null,
    var driver: String? = null
)