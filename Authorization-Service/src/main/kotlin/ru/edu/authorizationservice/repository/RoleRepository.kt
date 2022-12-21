package ru.edu.authorizationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.edu.authorizationservice.entity.RoleEntity

interface RoleRepository : JpaRepository<RoleEntity, Int> {
    fun findByName(name: String): RoleEntity
}