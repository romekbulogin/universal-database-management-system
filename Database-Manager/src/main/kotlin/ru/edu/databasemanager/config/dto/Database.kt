package ru.edu.databasemanager.config.dto

data class Database(
    var url: String? = null,
    var username: String? = null,
    var password: String? = null,
    var driverClassName: String? = null,
    var sqlCreateUser: String? = null
)