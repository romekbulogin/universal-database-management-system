package ru.edu.authorizationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.edu.authorizationservice.entity.UserEntity
import java.util.Optional

interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findByEmail(email: String): Optional<UserEntity>
}