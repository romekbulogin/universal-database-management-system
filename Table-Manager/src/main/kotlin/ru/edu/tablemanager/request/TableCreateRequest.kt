package ru.edu.tablemanager.request

import ru.edu.tablemanager.request.key.ForeignKey
import ru.edu.tablemanager.request.key.PrimaryKey

data class TableCreateRequest(
    var database: String? = null,
    var dbms: String? = null,
    var tableName: String? = null,
    var primaryKey: PrimaryKey? = null,
    var foreignKeys: List<ForeignKey>? = null,
    val uniqueAttributes: List<String>? = null,
    val defaultValues: HashMap<String, String>? = null,
    var columns: MutableList<ColumnTemplateRequest> = mutableListOf(),
)
