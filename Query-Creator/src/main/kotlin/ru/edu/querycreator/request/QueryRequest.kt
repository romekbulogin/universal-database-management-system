package ru.edu.querycreator.request

data class QueryRequest(
    var sql: String = "",
    var database: String = "",
    var dbms: String = "",
    var login: String = "",
)
