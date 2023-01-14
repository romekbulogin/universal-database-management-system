package ru.edu.queryexecutor.request

data class FindDatabases(
    var username: String = "",
    var dbms: String = ""
)
