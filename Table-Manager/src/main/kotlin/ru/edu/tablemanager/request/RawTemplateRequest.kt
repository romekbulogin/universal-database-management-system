package ru.edu.tablemanager.request

data class RawTemplateRequest(
    var name: String? = null,
    var dataType: String? = null,
    var length: Int? = null,
    var isNull: Boolean? = null,
    var default: Any? = null,
)
