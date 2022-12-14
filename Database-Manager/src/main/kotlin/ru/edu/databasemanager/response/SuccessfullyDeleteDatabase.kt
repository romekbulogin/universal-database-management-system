package ru.edu.databasemanager.response

data class SuccessfullyDeleteDatabase(
    var success: Boolean? = null,
    var comment: String? = null
)