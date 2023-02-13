package ru.edu.tablemanager.feign.authentication.request

data class AuthParamRequest(
    var database: String? = null,
    var dbms: String? = null
)
