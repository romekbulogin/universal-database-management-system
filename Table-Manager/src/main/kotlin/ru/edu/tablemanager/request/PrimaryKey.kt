package ru.edu.tablemanager.request

data class PrimaryKey(
    var name: String? = null,
    var dataType: String? = null,
    var length: Int? = null,
    var default: Any? = null,
)
