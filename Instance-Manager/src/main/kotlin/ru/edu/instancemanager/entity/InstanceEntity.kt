package ru.edu.instancemanager.entity

import jakarta.persistence.*

@Entity
@Table(name = "_instance")
data class InstanceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "url", nullable = false)
    var url: String? = null,
    @Column(name = "username", nullable = false)
    var username: String? = null,
    @Column(name = "password", nullable = false)
    var password: String? = null,
    @Column(name = "dbms", nullable = false)
    var dbms: String? = null,
    @Column(name = "sql_create_user", nullable = false)
    var sqlCreateUser: String? = null
)
