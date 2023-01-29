package ru.edu.databasemanager.response

data class SuccessfullyCreateDatabase(
    var success: Boolean? = true,
    var comment: String? = null,
    var url: String? = null,
    var username: String? = null,
    var password: String? = null
)
