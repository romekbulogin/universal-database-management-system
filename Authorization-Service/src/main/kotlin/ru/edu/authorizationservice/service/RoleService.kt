package ru.edu.authorizationservice.service

import org.springframework.stereotype.Service
import ru.edu.authorizationservice.entity.RoleEntity
import ru.edu.authorizationservice.repository.RoleRepository

@Service
class RoleService(private val roleRepository: RoleRepository) {
    fun save(roleEntity: RoleEntity) = roleRepository.save(roleEntity)
    fun findRoles(): MutableList<RoleEntity> = roleRepository.findAll()

    fun findByName(name: String): RoleEntity = roleRepository.findByName(name)
}