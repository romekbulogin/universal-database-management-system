package ru.edu.authorizationservice.feign.databasemanager.response.createddatabase

data class SuccessfullyCreateDatabase(
    var url: String? = null,
    var username: String? = null,
    var password: String? = null
)
