package ru.edu.authorizationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.edu.authorizationservice.entity.DatabaseEntity
import ru.edu.authorizationservice.entity.DatabaseEntityWithoutUser
import ru.edu.authorizationservice.entity.UserEntity

@Repository
interface DatabaseRepository : JpaRepository<DatabaseEntity, Int> {
    fun findDatabaseEntityByDatabaseNameAndAndDbmsAndAndUserEntity(
        database: String,
        dbms: String,
        userEntity: UserEntity
    ): DatabaseEntity

    fun findDatabaseEntityByDatabaseNameAndDbms(
        database: String,
        dbms: String,
    ): DatabaseEntity

    fun findAllByUserEntity(userEntity: UserEntity): List<DatabaseEntityWithoutUser>
}