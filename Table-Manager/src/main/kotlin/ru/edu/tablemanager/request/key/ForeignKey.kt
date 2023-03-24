package ru.edu.tablemanager.request.key

data class ForeignKey(
    override var name: String? = null,
    override var columnName: String? = null,
    override var dataType: String? = null,
    override var length: Int = 0,
    override var default: Any? = null,
    override var isIdentity: Boolean? = null,
    var referenceTableName: String? = null,
    var referenceColumnName: String? = null,
) : Key()
