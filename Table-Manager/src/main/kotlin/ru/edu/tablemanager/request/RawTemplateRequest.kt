package ru.edu.tablemanager.request

data class RawTemplateRequest(
    var name: String? = null,
    var dataType: String? = null,
    var length: Int? = null,
    var isNotNull: Boolean? = null,
    var isPrimaryKey: Boolean? = null,
    var defualt: String? = null,
)
