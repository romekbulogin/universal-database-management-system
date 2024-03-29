package ru.edu.authorizationservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "_databases")
data class DatabaseEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,
    var dbms: String? = null,
    var databaseName: String? = null,
    var login: String? = null,
    var passwordDbms: String? = null,
    @ManyToOne
    var userEntity: UserEntity? = null
)
