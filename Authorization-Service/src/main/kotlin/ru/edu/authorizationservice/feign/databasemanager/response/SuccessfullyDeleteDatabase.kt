package ru.edu.authorizationservice.feign.databasemanager.response

data class SuccessfullyDeleteDatabase(
    var success: Boolean? = null,
    var comment: String? = null
)