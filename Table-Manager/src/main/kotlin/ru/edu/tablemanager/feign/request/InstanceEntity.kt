package ru.edu.tablemanager.feign.request


data class InstanceEntity(
    var id: Int? = null,
    var url: String? = null,
    var username: String? = null,
    var password: String? = null,
    var dbms: String? = null,
    var sqlCreateUser: String? = null
)
