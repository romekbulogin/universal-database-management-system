package ru.edu.tablemanager.request

data class RawTemplateRequest(
    var name: String? = null,
    var dataType: String? = null,
    var length: Int = 0,
    var isNull: Boolean? = null,
)
