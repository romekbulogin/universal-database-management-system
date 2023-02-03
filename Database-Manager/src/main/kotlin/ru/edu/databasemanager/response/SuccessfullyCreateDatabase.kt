package ru.edu.databasemanager.response

data class SuccessfullyCreateDatabase(
    var url: String? = null,
    var username: String? = null,
    var password: String? = null
)
