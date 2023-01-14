package ru.edu.queryexecutor.request

data class QueryRequest(
    var sql: String = "",
    var database: String = "",
    var dbms: String = "",
    var login: String = "",
    var password: String = ""
)