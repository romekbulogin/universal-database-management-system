package ru.edu.authorizationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import ru.edu.authorizationservice.entity.UsersEntity

@Repository
interface UserRepository : JpaRepository<UsersEntity, Int> {
    fun findByUsername(username: String?): UsersEntity
}