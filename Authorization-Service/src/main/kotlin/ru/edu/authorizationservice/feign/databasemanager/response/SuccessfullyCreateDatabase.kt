package ru.edu.authorizationservice.feign.databasemanager.response

data class SuccessfullyCreateDatabase(
    var success: Boolean? = true,
    var comment: String? = null,
    var url: String? = null
)
