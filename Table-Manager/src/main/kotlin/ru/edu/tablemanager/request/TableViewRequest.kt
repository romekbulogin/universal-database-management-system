package ru.edu.tablemanager.request

data class TableViewRequest(
    var database: String?,
    var dbms: String?,
    var table: String,
)
