package ru.edu.authorizationservice.entity


import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class RoleEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Int? = null,
    var name: String? = null,
)
