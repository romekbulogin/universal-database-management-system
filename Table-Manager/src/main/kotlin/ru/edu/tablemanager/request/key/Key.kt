package ru.edu.tablemanager.request.key

abstract class Key {
    abstract var name: String?
    abstract var columnName: String?
    abstract var dataType: String?
    abstract var length: Int
    abstract var default: Any?
    abstract var isIdentity: Boolean?
}