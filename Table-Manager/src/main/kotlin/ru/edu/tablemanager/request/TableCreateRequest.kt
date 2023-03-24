package ru.edu.tablemanager.request

data class TableCreateRequest(
    var database: String? = null,
    var dbms: String? = null,
    var tableName: String? = null,
    var primaryKey: PrimaryKey? = null,
    val uniqueAttributes: List<String>? = null,
    val defaultValues: HashMap<String, String>? = null,
    var rawTable: MutableList<RawTemplateRequest> = mutableListOf(),
)
