package ru.edu.tablemanager.request

data class PrimaryKey(
    var name: String? = null,
    var columnName: String? = null,
    var dataType: String? = null,
    var length: Int = 0,
    var default: Any? = null,
    var isIdentity: Boolean? = null,
)
