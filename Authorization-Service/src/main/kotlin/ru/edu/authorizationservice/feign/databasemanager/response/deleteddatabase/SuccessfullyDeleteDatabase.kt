package ru.edu.authorizationservice.feign.databasemanager.response.deleteddatabase

data class SuccessfullyDeleteDatabase(
    var success: Boolean? = null,
    var comment: String? = null
)