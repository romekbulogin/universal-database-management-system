package ru.edu.authorizationservice.entity;

import java.sql.Timestamp;
import javax.persistence.*

@Entity
data class UsersEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Int? = null,
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var registrationDate: Timestamp? = null,
    @ManyToMany(fetch = FetchType.EAGER)
    var roles: Collection<RoleEntity> = mutableListOf()
)
