package ru.edu.tablemanager.feign.authentication.response

data class AuthParamResponse(
    var login: String? = null,
    var password: String? = null
)
